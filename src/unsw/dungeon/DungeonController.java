package unsw.dungeon;

import java.util.List;
import java.util.stream.Collectors;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import javafx.scene.image.Image;

import java.io.File;

/**
 * A controller for the dungeon.
 * It is responsible for managing entities,
 * publishing entity changes through entity wrappers to the view, or other subscribers,
 * and it is responsible for listening to goal completion.
 */
public class DungeonController {

    private DungeonView dungeonView;
    private final Dungeon dungeon;
    private final Goal goal;
    private boolean isGameComplete = false;
    private final Timeline timeline;
    private final Timeline timelineArrow;
    private final List<EntityWrapper> initialEntities;
    private Direction prevDirection = null;

    public DungeonController(Dungeon dungeon, Goal goal) {
        this.dungeon = dungeon;
        this.goal = goal;
        timeline = new Timeline(1);
        timeline.setCycleCount(Timeline.INDEFINITE);
        
        timelineArrow = new Timeline(40);
        timelineArrow.setCycleCount(Timeline.INDEFINITE);

        initialEntities = this.dungeon
                .getEntities()
                .stream()
                .map(this::onEntityLoad)
                .collect(Collectors.toUnmodifiableList());

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
     * Wraps listners around an entity that validate changes in entity state.
     * It also publishes changes in entity state to listeners.
     * @param entity: The entity to be processed
     * @return A wrapped entity that can be subscribed to.
     */
    public EntityWrapper onEntityLoad(Entity entity) {
        EntityWrapper entityWrapper = new EntityWrapper(entity);
        entity.x().addListener((ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) -> {
            if (!isWithinRange(newValue.intValue(), dungeon.getWidth())) {
                if (isWithinRange(oldValue.intValue(), dungeon.getWidth())) {
                    entity.x().setValue(oldValue);
                }
            } else {
                entityWrapper.setPosition(entity.getPosition());
            }
        });
        entity.y().addListener((ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) -> {
            if (!isWithinRange(newValue.intValue(), dungeon.getHeight())) {
                if (isWithinRange(oldValue.intValue(), dungeon.getHeight())) {
                    entity.y().setValue(oldValue);
                }
            } else {
                entityWrapper.setPosition(entity.getPosition());
            }
        });
        entity.isDeletedProperty().addListener((ObservableValue<? extends Boolean> observable,
                                                Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                this.dungeon.removeEntity(entity);
                entityWrapper.setDeleted();
                if (entityWrapper.entityClass.equals(Player.class)) {
                    System.out.println("Player Died.");
                }
            }
        });
        entity.status().addListener((ObservableValue<? extends String> observable,
                                     String oldValue, String newValue) -> entityWrapper.publishStatusUpdate(newValue));
        if (entity instanceof Enemy) {
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                    actionEvent -> {
                        if (!entity.isDeleted()) {
                            ((Enemy) entity).moveEnemy();
                        }
                    }
            ));
        } else if (entity instanceof Arrow){
            timelineArrow.getKeyFrames().add(new KeyFrame(Duration.millis(25),
                    actionEvent -> {
                            ((Arrow) entity).moveArrow(directionFacing());
                            System.out.println((entity.getPosition()));
                            
                        
       //                 }
       //             }
       //     ));
                    }
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
                player.shootArrow(prevDirection);
                
                Arrow arrow = new Arrow(player.getX()+1, player.getY(), player.getDungeon());
                EntityWrapper wrappedArrow = onEntityLoad(arrow);

                thisDungeonView().updateSquares(arrow, wrappedArrow, new Image((new File("images/arrow.png")).toURI().toString()));
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

    public Direction directionFacing(){
        return prevDirection;
    }

    public DungeonView thisDungeonView(){
        return dungeonView;
    }

    public DungeonView loadDungeonView() {
        dungeonView = new DungeonView(dungeon.getHeight(), dungeon.getWidth(), this, initialEntities);
        timeline.play();
        return dungeonView;
    }
}

