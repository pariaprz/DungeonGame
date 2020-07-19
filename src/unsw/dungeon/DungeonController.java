package unsw.dungeon;

import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;

/**
 * A JavaFX controller for the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonController {

    private DungeonView dungeonView;
    private Dungeon dungeon;
    private Goal goal;

    public DungeonController(Dungeon dungeon) {
        this.dungeon = dungeon;
        this.goal = dungeon.getGoal();
        List<EntityWrapper> initialEntities = this.dungeon
                .getEntities()
                .stream()
                .map(this::onEntityLoad)
                .collect(Collectors.toUnmodifiableList());

        goal.getCompleteProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) System.out.println("Level Complete");
        });
        this.dungeonView = new DungeonView(dungeon.getHeight(), dungeon.getWidth(), this, initialEntities);
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
                System.out.println("Class deleted: " + entity.getClass());
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
            for (Enemy e : dungeon.getEnemies()){
                e.moveEnemy();
            }
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

