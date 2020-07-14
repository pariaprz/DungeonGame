package unsw.dungeon;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.util.Map;

/**
 * A DungeonLoader that also creates the necessary ImageViews for the UI,
 * connects them via listeners to the model, and creates a controller.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonControllerLoader extends DungeonLoader {

    private List<ImageView> entities;

    //Images
    private Image keyImage;
    private Image exitImage;
    private Image wallImage;
    private Image swordImage;
    private Image enemyImage;
    private Image playerImage;
    private Image switchImage;
    private Image portalImage;
    private Image boulderImage;
    private Image openDoorImage;
    private Image treasureImage;
    private Image closedDoorImage;
    private Image invincibilityImage;

    private Map<Class<? extends Entity>, Image> imageMap;

    public DungeonControllerLoader(String filename)
            throws FileNotFoundException {
        super(filename);
        entities = new ArrayList<>();

        imageMap = new HashMap<>();
        imageMap.put(Key.class, new Image((new File("images/key.png")).toURI().toString()));
        imageMap.put(Exit.class, new Image((new File("images/exit.png")).toURI().toString()));
        imageMap.put(Portal.class, new Image((new File("images/portal.png")).toURI().toString()));
        imageMap.put(Boulder.class, new Image((new File("images/boulder.png")).toURI().toString()));
        imageMap.put(Door.class, new Image((new File("images/closed_door.png")).toURI().toString()));
        imageMap.put(Player.class, new Image((new File("images/human_new.png")).toURI().toString()));
        imageMap.put(Treasure.class, new Image((new File("images/gold_pile.png")).toURI().toString()));
        imageMap.put(Wall.class, new Image((new File("images/brick_brown_0.png")).toURI().toString()));
        imageMap.put(Switch.class, new Image((new File("images/pressure_plate.png")).toURI().toString()));
        imageMap.put(Sword.class, new Image((new File("images/greatsword_1_new.png")).toURI().toString()));
        imageMap.put(Enemy.class, new Image((new File("images/deep_elf_master_archer.png")).toURI().toString()));
        imageMap.put(Invincibility.class, new Image((new File("images/brilliant_blue_new.png")).toURI().toString()));

        openDoorImage = new Image((new File("images/open_door.png")).toURI().toString());
    }

    @Override
    public void onLoad(Entity entity) {
        ImageView view = new ImageView(imageMap.get(entity.getClass()));
        addEntity(entity, view);
    }

    private void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        entities.add(view);
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
    private void trackPosition(Entity entity, Node node) {
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());
        entity.x().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        });
        entity.y().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        });
    }

    /**
     * Create a controller that can be attached to the DungeonView with all the
     * loaded entities.
     * @return
     * @throws FileNotFoundException
     */
    public DungeonController loadController() throws FileNotFoundException {
        return new DungeonController(load(), entities);
    }


}
