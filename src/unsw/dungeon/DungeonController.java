package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;

/**
 * A controller for the dungeon.
 * It is responsible for managing entities,
 * publishing entity changes through entity wrappers to the view, or other subscribers,
 * and it is responsible for listening to goal completion.
 */
public class DungeonController {

    private DungeonView dungeonView;
    private InstructionScreen instructionScreen;
    private Dungeon dungeon;
    private Goal goal;
    private boolean isGameComplete = false;
    private Timer timeline;
    private boolean pause = true;
    private List<EntityWrapper> initialEntities;
    private Direction prevDirection = Direction.RIGHT;

    public DungeonController() {
        timeline = new Timer("Slayable");
        initialEntities = new ArrayList<>();
    }

    private void setup(Dungeon dungeon, Goal goal) {
        this.dungeon = dungeon;
        this.goal = goal;

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
                pause = true;
                isGameComplete = true;
            }
        });
    }

    private void addScheduledEvent(Runnable f, int timeInMillis) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (!pause) {
                        f.run();
                    }
                });
            }
        };
        timeline.scheduleAtFixedRate(task, timeInMillis, timeInMillis);
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
                    pause();
                }
            }
        });
        entity.status().addListener((observable, oldValue, newValue) -> entityWrapper.publishStatusUpdate(newValue));
        if (entity instanceof Slayable){
            addScheduledEvent(() -> {
                if (!entity.isDeleted()) {
                    ((Slayable) entity).move();
                }
            }, ((Slayable) entity).getSpeedInMillis());
        } else if (entity instanceof Arrow){
            addScheduledEvent(() -> {
                if (!entity.isDeleted()) {
                    ((Arrow) entity).moveArrow();
                }
            }, 25);
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
            if (player.status().get().equals(Player.ARMED_STATUS)){
                System.out.println("Swinging sword");
                player.swingSword();
            } else if (player.status().get().equals(Player.RANGER_STATUS)){
                System.out.println("Shooting arrow");
                player.shootArrow();
                Arrow arrow = new Arrow(player.getX(), player.getY(), prevDirection, dungeon);
                EntityWrapper wrappedArrow = onEntityLoad(arrow);
                thisDungeonView().addEntity(wrappedArrow);
                arrow.setDirectionStatus();
                play();
            }
                
            break;
        case Z:
            player.changeWeapons();
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
        pause = true;
    }

    public void play() {
        pause = false;
    }

    public void loadDungeon(Dungeon dungeon, Goal goal) {
        this.initialEntities.forEach(EntityWrapper::dropAllSubscribers);
        this.timeline.cancel();
        this.timeline.purge();
        this.timeline = new Timer();
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

    public void setInstructionScreen(InstructionScreen instructionScreen){
        this.instructionScreen = instructionScreen;
    }

    public InstructionScreen getInstructionScreen(){
        return instructionScreen;
    }
}

