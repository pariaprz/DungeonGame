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

    private List<EntityWrapper> entities;

    private DungeonView dungeonView;

    private Player player;

    private Dungeon dungeon;

    public DungeonController(Dungeon dungeon) {
        this.dungeon = dungeon;
        this.player = dungeon.getPlayer();
        this.dungeonView = new DungeonView(dungeon.getHeight(), dungeon.getWidth());
        entities = this.dungeon.getEntities().stream().map(this::onEntityLoad).collect(Collectors.toList());
    }

    public EntityWrapper onEntityLoad(Entity entity) {
        EntityWrapper entityWrapper = new EntityWrapper(entity.getClass());
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
        dungeonView.onEntityLoad(entityWrapper);
        return entityWrapper;
    }

    private static boolean isWithinRange(int number, int maxValue) {
        return number < maxValue && number >= 0;
    }

    @FXML
    public void initialize() {
        dungeonView.initialize();
    }

    @FXML
    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
        case UP:
        case DOWN:
        case LEFT:
        case RIGHT:
            player.handleDirectionKey(event.getCode());
            
            for (Enemy e : dungeon.getEnemies()){
                e.MoveEnemy();
            }
            
        default: 
            break;
        }
    }

}

