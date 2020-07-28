package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

/**
 * A controller for the dungeon.
 * It is responsible for managing entities,
 * publishing entity changes through entity wrappers to the view, or other subscribers,
 * and it is responsible for listening to goal completion.
 */
public class DungeonController {

    private DungeonView dungeonView;
    private Dungeon dungeon;
    private Goal goal;
    private boolean isGameComplete = false;
    private final Timeline timeline;
    private final Timeline timelineArrow;
    private List<EntityWrapper> initialEntities;
    private Direction prevDirection = Direction.RIGHT;

    public DungeonController() {
        timeline = new Timeline(20);
        timelineArrow = new Timeline(40);
        initialEntities = new ArrayList<>();
    }

    private void setup(Dungeon dungeon, Goal goal) {
        this.dungeon = dungeon;
        this.goal = goal;

        timeline.setCycleCount(Timeline.INDEFINITE);
        timelineArrow.setCycleCount(Timeline.INDEFINITE);

        initialEntities = this.dungeon
                .getEntities()
                .stream()
                .map(this::onEntityLoad)
                .collect(Collectors.toUnmodifiableList());

        goal.computeComplete(dungeon);
        goal.getCompleteProperty().addListener((ObservableValue<? extends Boolean> observable,
                                                Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                System.out.println("Level Complete");
                timeline.stop();
                isGameComplete = true;
            }
        });
    }

    /**
     * Wraps listeners around an entity that validate changes in entity state.
     * It also publishes changes in entity state to listeners.
     * @param entity: The entity to be processed
     * @return A wrapped entity that can be subscribed to.
     */
    public EntityWrapper onEntityLoad(Entity entity) {
        EntityWrapper entityWrapper = new EntityWrapper(entity);
        entity.x().addListener((observable, oldValue, newValue) -> {
            if (!isWithinRange(newValue.intValue(), dungeon.getWidth())) {
                if (isWithinRange(oldValue.intValue(), dungeon.getWidth())) {
                    entity.x().setValue(oldValue);
                }
            } else {
                entityWrapper.setPosition(entity.getPosition());
            }
        });
        entity.y().addListener((observable, oldValue, newValue) -> {
            if (!isWithinRange(newValue.intValue(), dungeon.getHeight())) {
                if (isWithinRange(oldValue.intValue(), dungeon.getHeight())) {
                    entity.y().setValue(oldValue);
                }
            } else {
                entityWrapper.setPosition(entity.getPosition());
            }
        });
        entity.isDeletedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.dungeon.removeEntity(entity);
                entityWrapper.setDeleted();
                if (entityWrapper.entityClass.equals(Player.class)) {
                    System.out.println("Player Died.");
                }
            }
        });
        entity.status().addListener((observable, oldValue, newValue) -> entityWrapper.publishStatusUpdate(newValue));
        if (entity instanceof Enemy) {
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500),
                    actionEvent -> {
                        if (!entity.isDeleted()) {
                            ((Enemy) entity).moveEnemy();
                        }
                    }
            ));
        } else if (entity instanceof Arrow){
            timelineArrow.getKeyFrames().add(new KeyFrame(Duration.millis(25),
                    actionEvent -> ((Arrow) entity).moveArrow()
            ));
        }
        goal.attachListener(entityWrapper, dungeon);
        return entityWrapper;
    }

    private static boolean isWithinRange(int number, int maxValue) {
        return number < maxValue && number >= 0;
    }

    public boolean isGameComplete() {
        return isGameComplete;
    }

    public void handleKeyPress(KeyCode keyCode) {
        Player player = dungeon.getPlayer();
        if (isGameComplete || player == null) {
            return;
        }
        switch (keyCode) {
        case UP:
        case DOWN:
        case LEFT:
        case RIGHT:
            player.handleDirectionKey(keyCode);
            prevDirection = Direction.fromKeyCode(keyCode);
            break;
        case SPACE:
            if (player.hasSword()){
                System.out.println("Swinging sword");
                player.swingSword();
            } else if(player.hasBow() && player.getArrowCount() > 0){
                System.out.println("Shooting arrow");
                player.shootArrow();
                Arrow arrow = new Arrow(player.getX(), player.getY(), prevDirection, dungeon);
                EntityWrapper wrappedArrow = onEntityLoad(arrow);
                thisDungeonView().addEntity(wrappedArrow);
                arrow.setDirectionStatus();

                timelineArrow.play();
            }
            break;
        default:
            break;
        }
        System.out.println("Facing" + prevDirection);
    }

    public List<EntityWrapper> getInitialEntities() {
        return initialEntities;
    }

    public Goal getGoal() {
        return goal;
    }

    public void pause() {
        timeline.pause();
        timelineArrow.pause();
    }

    public void play() {
        timeline.play();
        timelineArrow.play();
    }

    public void loadDungeon(Dungeon dungeon, Goal goal) {
        this.initialEntities.forEach(EntityWrapper::dropAllSubscribers);
        this.timelineArrow.getKeyFrames().clear();
        this.timeline.getKeyFrames().clear();
        this.isGameComplete = false;

        setup(dungeon, goal);
        dungeonView.loadDungeon(dungeon.getWidth(), dungeon.getHeight(), initialEntities);
        play();
    }

    public DungeonView thisDungeonView(){
        return dungeonView;
    }

    public DungeonView loadDungeonView() {
        dungeonView = new DungeonView(this);
        play();
        return dungeonView;
    }
}

