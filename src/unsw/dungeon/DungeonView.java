package unsw.dungeon;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.util.Map;

/**
 * A DungeonLoader that also creates the necessary ImageViews for the UI,
 * connects them via listeners to the model, and creates a controller.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonView {
    private static String DEFAULT_IMG = "DEFAULT_IMAGE";

    private Map<Class<? extends Entity>, Map<String, Image>> imageMap;
    private DungeonController controller;
    private List<EntityWrapper> initialEntities;
    private List<ImageView> entities;
    private final int height, width;

    @FXML
    private GridPane squares;

    public DungeonView(int height, int width, DungeonController controller, List<EntityWrapper> initialEntities)  {
        this.height = height;
        this.width = width;
        this.controller = controller;
        this.initialEntities = initialEntities;
        imageMap = initialiseImageMap();
        entities = new ArrayList<>();
    }

    private void onEntityLoad(EntityWrapper entity) {
        ImageView view = new ImageView(imageMap.get(entity.entityClass).get(DEFAULT_IMG));
        track(entity, view);
        entities.add(view);
    }

    @FXML
    public void initialize() {
        Image ground = new Image((new File("images/dirt_0_new.png")).toURI().toString());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                squares.add(new ImageView(ground), x, y);
            }
        }

        initialEntities.forEach(this::onEntityLoad);
        for (ImageView entity : entities) {
            squares.getChildren().add(entity);
        }
    }
    /**
     * Set a node in a GridPane to have its position track the position of an
     * entity in the dungeon.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the
     * model will automatically be reflected in the view.
     * @param entity
     * @param node
     */
    private void track(EntityWrapper entity, ImageView node) {
        GridPane.setColumnIndex(node, entity.getPosition().x);
        GridPane.setRowIndex(node, entity.getPosition().y);
        entity.addPositionObserver((PropertyChangeEvent evt) -> {
            Position newPosition = (Position)evt.getNewValue();
            GridPane.setColumnIndex(node, newPosition.x);
            GridPane.setRowIndex(node, newPosition.y);
        });
        entity.addDeleteObserver((PropertyChangeEvent evt) -> {
            squares.getChildren().remove(node);
            entity.dropAllSubscribers();
        });
        entity.addStateObserver((PropertyChangeEvent evt) -> {
            String update = (String)(evt.getNewValue());
            Image defaultImg = imageMap.get(entity.entityClass).get(DEFAULT_IMG);
            node.setImage(imageMap.get(entity.entityClass).getOrDefault(update, defaultImg));
        });
    }

    @FXML
    public void handleKeyPress(KeyEvent event) {
        controller.handleKeyPress(event);
    }

    private static Map<Class<? extends Entity>, Map<String, Image>> initialiseImageMap() {
        Map<Class<? extends Entity>, Map<String, Image>> imageMap = new HashMap<>();
        imageMap.put(Key.class, Map.of(DEFAULT_IMG, new Image((new File("images/key.png")).toURI().toString())));
        imageMap.put(Exit.class, Map.of(DEFAULT_IMG, new Image((new File("images/exit.png")).toURI().toString())));
        imageMap.put(Portal.class, Map.of(DEFAULT_IMG, new Image((new File("images/portal.png")).toURI().toString())));
        imageMap.put(Boulder.class, Map.of(DEFAULT_IMG, new Image((new File("images/boulder.png")).toURI().toString())));
        imageMap.put(Treasure.class, Map.of(DEFAULT_IMG, new Image((new File("images/gold_pile.png")).toURI().toString())));
        imageMap.put(Wall.class, Map.of(DEFAULT_IMG, new Image((new File("images/brick_brown_0.png")).toURI().toString())));
        imageMap.put(Switch.class, Map.of(DEFAULT_IMG, new Image((new File("images/pressure_plate.png")).toURI().toString())));
        imageMap.put(Sword.class, Map.of(DEFAULT_IMG, new Image((new File("images/greatsword_1_new.png")).toURI().toString())));
        imageMap.put(Enemy.class, Map.of(DEFAULT_IMG, new Image((new File("images/deep_elf_master_archer.png")).toURI().toString())));
        imageMap.put(Invincibility.class, Map.of(DEFAULT_IMG, new Image((new File("images/brilliant_blue_new.png")).toURI().toString())));
        imageMap.put(Door.class, Map.of(
                DEFAULT_IMG, new Image((new File("images/closed_door.png")).toURI().toString()),
                Door.OPEN_STATUS, new Image((new File("images/open_door.png")).toURI().toString())
        ));
        imageMap.put(Player.class, Map.of(
                DEFAULT_IMG, new Image((new File("images/human_new.png")).toURI().toString()),
                Player.INVINCIBLE_STATUS, new Image((new File("images/pacman.png")).toURI().toString()),
                Player.ARMED_STATUS, new Image((new File("images/armed_player.png")).toURI().toString())
        ));
        return imageMap;
    }
}
