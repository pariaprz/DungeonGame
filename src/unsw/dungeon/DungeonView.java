package unsw.dungeon;

import java.beans.PropertyChangeEvent;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.io.File;

/**
 * A DungeonLoader that also creates the necessary ImageViews for the UI,
 * connects them via listeners to the model, and creates a controller.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonView {
    private static String DEFAULT_IMG = "DEFAULT_IMAGE";

    private final Map<Class<? extends Entity>, Map<String, Image>> imageMap;
    private final DungeonController controller;
    private final List<EntityWrapper> initialEntities;
    private final List<ImageView> entities;
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

    public void onEntityLoad(EntityWrapper entity) {
        ImageView view = new ImageView(imageMap.get(entity.entityClass).get(DEFAULT_IMG));
        track(entity, view);
        entities.add(view);
    }

    @FXML
    public void initialize() {
        Image ground = new Image((new File("images/dirt_0_new.png")).toURI().toString());
        Image inv_1sec = new Image((new File("images/Invincibility_Time_Icons/invincibility_1sec.png")).toURI().toString());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                squares.add(new ImageView(ground), x, y);
            }
        }

        initialEntities.forEach(this::onEntityLoad);
        for (ImageView entity : entities) {
            squares.getChildren().add(entity);
        }

        squares.add(new ImageView(inv_1sec), 4, 0);
    }

    @FXML
    public void updateSquares(Entity entity, EntityWrapper wrappedEntity, Image image){
        ImageView newEntity = new ImageView(image);
        squares.getChildren().add(newEntity);
        track(wrappedEntity, newEntity);
        entities.add(newEntity);
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
    public void track(EntityWrapper entity, ImageView node) {
        GridPane.setColumnIndex(node, entity.getPosition().x);
        GridPane.setRowIndex(node, entity.getPosition().y);
        entity.addPositionObserver((PropertyChangeEvent event) -> {
            Position newPosition = (Position)event.getNewValue();
            GridPane.setColumnIndex(node, newPosition.x);
            GridPane.setRowIndex(node, newPosition.y);
        });
        entity.addDeleteObserver((PropertyChangeEvent event) -> {
            squares.getChildren().remove(node);
            entity.dropAllSubscribers();
        });
        entity.addStatusObserver((PropertyChangeEvent event) -> {
            String update = Optional.of((String)(event.getNewValue())).orElse(DEFAULT_IMG);
            Image defaultImg = imageMap.get(entity.entityClass).get(DEFAULT_IMG);
            System.out.println("getting img: " + update);
            node.setImage(imageMap.get(entity.entityClass).getOrDefault(update, defaultImg));
        });
    }

    @FXML
    public void handleKeyPress(KeyEvent event) {
        controller.handleKeyPress(event.getCode());
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
        imageMap.put(WallWalker.class, Map.of(DEFAULT_IMG, new Image((new File("images/wallwalker.png")).toURI().toString())));
        imageMap.put(Bow.class, Map.of(DEFAULT_IMG, new Image((new File("images/bow.png")).toURI().toString())));
        imageMap.put(Arrow_Consumable.class, Map.of(DEFAULT_IMG, new Image((new File("images/arrow_directions/arrow_right.png")).toURI().toString())));
        imageMap.put(Door.class, Map.of(
                DEFAULT_IMG, new Image((new File("images/closed_door.png")).toURI().toString()),
                Door.OPEN_STATUS, new Image((new File("images/open_door.png")).toURI().toString())
        ));
        imageMap.put(Player.class, Map.of(
                DEFAULT_IMG, new Image((new File("images/human_new.png")).toURI().toString()),
                Player.INVINCIBLE_STATUS, new Image((new File("images/gnome.png")).toURI().toString()),
                Player.ARMED_STATUS, new Image((new File("images/player_with_sword.png")).toURI().toString()),
                Player.WALLWALKER_STATUS, new Image((new File("images/wallwalker_player.png")).toURI().toString()),
                Player.RANGER_STATUS, new Image((new File("images/ranger.png")).toURI().toString())
        ));

        return imageMap;
    }
}
