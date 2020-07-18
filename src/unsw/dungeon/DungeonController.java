package unsw.dungeon;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

/**
 * A JavaFX controller for the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonController {

    private List<EntityWrapper> initialEntities;
    private DungeonView dungeonView;
    private Player player;
    private Dungeon dungeon;

    public DungeonController(Dungeon dungeon) {
        this.dungeon = dungeon;
        this.player = dungeon.getPlayer();
        initialEntities = this.dungeon
                .getEntities()
                .stream()
                .map(this::onEntityLoad)
                .collect(Collectors.toUnmodifiableList());
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
                System.out.println("Changing position");
                entityWrapper.setPosition(entity.getPosition());
            }
        });
        entity.isDeletedProperty().addListener((ObservableValue<? extends Boolean> observable,
                                                Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                this.dungeon.removeEntity(entity);
                entityWrapper.setDeleted();
            }
        });
        entity.status().addListener((ObservableValue<? extends String> observable,
                                     String oldValue, String newValue) -> {
            entityWrapper.publishStatusUpdate(newValue);
        });
        return entityWrapper;
    }

    private static boolean isWithinRange(int number, int maxValue) {
        return number < maxValue && number >= 0;
    }

    @FXML
    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
        case UP:
        case DOWN:
        case LEFT:
        case RIGHT:
            for (Enemy e : dungeon.getEnemies()){
                e.MoveEnemy();
            }    
        
            player.handleDirectionKey(event.getCode());
        default:
            break;
        }
    }

    public DungeonView getDungeonView() {
        return dungeonView;
    }
}

