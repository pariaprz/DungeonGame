package unsw.dungeon;

import java.beans.PropertyChangeEvent;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.w3c.dom.Node;

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

    private final Map<Class<? extends Entity>, Label> goals;

    @FXML
    private GridPane squares;

    public DungeonView(int height, int width, DungeonController controller, List<EntityWrapper> initialEntities)  {
        this.height = height;
        this.width = width;
        this.controller = controller;
        this.initialEntities = initialEntities;
        imageMap = initialiseImageMap();
        goals = initialiseGoalsMap(controller.getGoal());
        entities = new ArrayList<>();
    }

    private void onEntityLoad(EntityWrapper entity) {
        ImageView view = new ImageView(imageMap.get(entity.entityClass).get(DEFAULT_IMG));
        trackEntities(entity, view);
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

        int[] xPosition = { 0 };
        int yPosition = height+1;
        goals.forEach((entity, label) -> {
            if (entity == Exit.class) return;
            squares.add(new ImageView(imageMap.get(entity).get(DEFAULT_IMG)), xPosition[0]++, yPosition);
            squares.add(label, xPosition[0], yPosition, 2, 1);
            xPosition[0] += 2;
        });
        if (goals.get(Exit.class) != null) {
            squares.add(new ImageView(imageMap.get(Exit.class).get(DEFAULT_IMG)), xPosition[0]++, yPosition);
            squares.add(goals.get(Exit.class), xPosition[0], yPosition, 5, 1);
        }
        squares.add(new ImageView(inv_1sec), 4, 0);
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
    private void trackEntities(EntityWrapper entity, ImageView node) {
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

    private Map<Class<? extends Entity>, Label> initialiseGoalsMap(Goal goal) {
        Map<Class<? extends Entity>, Label> goalMap = new HashMap<>();
        goal.observeableTopics().forEach((entry) -> {
            if (goalMap.containsKey(entry.getKey())) {
                return;
            }

            goalMap.put(entry.getKey(), new Label(String.valueOf(entry.getValue().get())));
            if (entry.getKey() == Exit.class) {
                goalMap.get(entry.getKey())
                        .setText(goal.getGoalEngine() instanceof ANDGoalEngine ? "Complete other tasks" : "Get to the exit!");
                return;
            }
            entry.getValue().addListener((observableValue, oldValue, newValue) -> {
                goalMap.get(entry.getKey()).setText(String.valueOf((int)newValue == 0 ? "Complete" : newValue));
                boolean onlyExitRemaining = goalMap.get(Exit.class) != null && goalMap.values().stream()
                        .map(Label::getText)
                        .filter(text -> !text.equalsIgnoreCase("Complete"))
                        .count() <= 1;
                if (onlyExitRemaining) {
                    goalMap.get(Exit.class).setText("Get to the exit!");
                }
            });
            goalMap.get(entry.getKey()).setText(String.valueOf(entry.getValue().get()));
        });
        return goalMap;
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
        imageMap.put(Door.class, Map.of(
                DEFAULT_IMG, new Image((new File("images/closed_door.png")).toURI().toString()),
                Door.OPEN_STATUS, new Image((new File("images/open_door.png")).toURI().toString())
        ));
        imageMap.put(Player.class, Map.of(
                DEFAULT_IMG, new Image((new File("images/human_new.png")).toURI().toString()),
                Player.INVINCIBLE_STATUS, new Image((new File("images/gnome.png")).toURI().toString()),
                Player.ARMED_STATUS, new Image((new File("images/player_with_sword.png")).toURI().toString()),
                Player.WALLWALKER_STATUS, new Image((new File("images/wallwalker_player.png")).toURI().toString())
        ));

        return imageMap;
    }
}
