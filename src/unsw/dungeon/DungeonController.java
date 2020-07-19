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

/**
 * A JavaFX controller for the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonController {

    private DungeonView dungeonView;
    private final Dungeon dungeon;
    private final Goal goal;
    private boolean isGameComplete = false;
    private final Timeline timeline;
    private final List<EntityWrapper> initialEntities;

    public DungeonController(Dungeon dungeon, Goal goal) {
        this.dungeon = dungeon;
        this.goal = goal;
        timeline = new Timeline(2);
        timeline.setCycleCount(Timeline.INDEFINITE);

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
            break;
        case SPACE:
            System.out.println("Swinging sword");
            player.swingSword();
            break;
        default:
            break;
        }
    }

    public List<EntityWrapper> getInitialEntities() {
        return initialEntities;
    }

    public Goal getGoal() {
        return goal;
    }

    public DungeonView loadDungeonView() {
        dungeonView = new DungeonView(dungeon.getHeight(), dungeon.getWidth(), this, initialEntities);
        timeline.play();
        return dungeonView;
    }
}

