package unsw.dungeon;

import java.util.List;
import java.util.stream.Collectors;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

/**
 * A JavaFX controller for the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonController {

    private final DungeonView dungeonView;
    private final Dungeon dungeon;
    private final Goal goal;
    private final Timeline timeline;

    public DungeonController(Dungeon dungeon, Goal goal) {
        this.dungeon = dungeon;
        this.goal = goal;
        timeline = new Timeline(2);
        timeline.setCycleCount(Timeline.INDEFINITE);

        System.out.println(this.dungeon);
        List<EntityWrapper> initialEntities = this.dungeon
                .getEntities()
                .stream()
                .map(this::onEntityLoad)
                .collect(Collectors.toUnmodifiableList());

        goal.getCompleteProperty().addListener((ObservableValue<? extends Boolean> observable,
                                                Boolean oldValue, Boolean newValue) -> {
            if (newValue) System.out.println("Level Complete");
        });
        this.dungeonView = new DungeonView(dungeon.getHeight(), dungeon.getWidth(), this, initialEntities);
        timeline.play();
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
                                     String oldValue, String newValue) -> {
            entityWrapper.publishStatusUpdate(newValue);
        });
        if (entity instanceof Enemy) {
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                    actionEvent -> ((Enemy) entity).moveEnemy()
            ));
        }
        goal.attachListener(entityWrapper, dungeon);
        return entityWrapper;
    }

    private static boolean isWithinRange(int number, int maxValue) {
        return number < maxValue && number >= 0;
    }

    @FXML
    public void handleKeyPress(KeyEvent event) {
        Player player = dungeon.getPlayer();
        if (goal.computeComplete(dungeon) || player == null) {
            return;
        }
        switch (event.getCode()) {
        case UP:
        case DOWN:
        case LEFT:
        case RIGHT:
            player.handleDirectionKey(event.getCode());
            break;
        case SPACE:
            System.out.println("Swinging sword");
            player.swingSword();
            break;
        default:
            break;
        }
    }

    public DungeonView getDungeonView() {
        return dungeonView;
    }
}

