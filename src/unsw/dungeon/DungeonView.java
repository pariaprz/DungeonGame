package unsw.dungeon;

import java.beans.PropertyChangeEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;

/**
 * A DungeonLoader that also creates the necessary ImageViews for the UI,
 * connects them via listeners to the model, and creates a controller.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonView {
    public static String DEFAULT_IMG = "DEFAULT_IMAGE";

    private static List<Pair<String, String>> LEVELS = List.of(
            new Pair<>("Maze", "maze.json"), new Pair<>("Boulders", "boulders.json"),
            new Pair<>("Treasure Galore", "treasure_galore.json"), new Pair<>("Fancy!", "fancy_goal.json"),
            new Pair<>("Test", "testing.json"));
    private final Map<Class<? extends Entity>, Map<String, Image>> imageMap;
    private final DungeonController controller;
    private DungeonDisplay display;
    private String selectedLevel;
    private List<EntityWrapper> initialEntities;
    private List<ImageView> entities;
    private int height, width;

    private Map<Class<? extends Entity>, Label> goals;

    @FXML
    private GridPane squares;
    @FXML
    private Text completeText;
    @FXML
    private GridPane pauseMenu;

    @FXML
    private GridPane mainMenu;

    public DungeonView(DungeonController controller)  {
        this.controller = controller;
        imageMap = initialiseImageMap();
        initialEntities = new ArrayList<>();
        this.display = new DungeonDisplay(imageMap);

    }

    private void setup(int width, int height, List<EntityWrapper> initialEntities) {
        this.height = height;
        this.width = width;
        this.initialEntities = initialEntities;
        this.goals = initialiseGoalsMap(controller.getGoal());
        this.entities = new ArrayList<>();
    }

    public void onEntityLoad(EntityWrapper entity) {
        ImageView view = new ImageView(imageMap.get(entity.entityClass).get(DEFAULT_IMG));
        trackEntities(entity, view);
        entities.add(view);
    }

    @FXML
    public void initialize() {
        initialEntities.forEach(this::onEntityLoad);
        display.setGridpane(squares);
        display.showMainMenu(mainMenu, LEVELS, this::selectDungeon);
    }

    @FXML
    public void addEntity(EntityWrapper entity){
        ImageView newEntity = new ImageView(imageMap.get(entity.entityClass).get(DEFAULT_IMG));
        trackEntities(entity, newEntity);
        entities.add(newEntity);
        display.updateSquares(newEntity);
    }

    public void loadDungeon(int width, int height, List<EntityWrapper> initialEntities) {
        setup(width, height, initialEntities);
        initialEntities.forEach(this::onEntityLoad);
        display.setGridpane(squares);
        display.addGoalIcons((FlowPane) pauseMenu.lookup("#goal-icons"), goals);
        display.setGoalString((TextFlow) pauseMenu.lookup("#goal-list"), controller.getGoal());
        display.initializeDungeon(width, height, entities, goals);
    }

    public boolean selectDungeon(String dungeonName) {
        String filename = "dungeons/" + dungeonName;
        try {
            JSONObject json = new JSONObject(new JSONTokener(new FileReader(filename)));
            DungeonLoader dungeonLoader = new DungeonLoader(json);
            controller.loadDungeon(dungeonLoader.load(), dungeonLoader.loadGoal());
            selectedLevel = dungeonName;
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Invalid dungeon name. Path: '" + filename + "' not found.");
        }
        return false;
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
            if (entity.entityClass.equals(Player.class)) {
                display.addFinalText(completeText, "You Died.", "0xF44336");
            }
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
        switch (event.getCode()) {
            case Q:
                System.exit(0);
            case M:
                display.showMainMenu(mainMenu, LEVELS, this::selectDungeon);
                break;
            case R:
                if (selectedLevel != null) {
                    selectDungeon(selectedLevel);
                }
                break;
            case P:
                if (pauseMenu.isVisible()) {
                    pauseMenu.setVisible(false);
                    controller.play();
                } else {
                    pauseMenu.setVisible(true);
                    controller.pause();
                }
                break;
            default:
                if (pauseMenu.isVisible()) return;
                controller.handleKeyPress(event.getCode());
        }
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
                goalMap.get(entry.getKey()).setFont(new Font("Default", 5));
                return;
            }
            entry.getValue().addListener((observableValue, oldValue, newValue) -> {
                goalMap.get(entry.getKey()).setText(String.valueOf((int)newValue == 0 ? "Complete" : newValue));
                goalMap.get(entry.getKey()).setFont(new Font("Default", 5));
                boolean onlyExitRemaining = goalMap.get(Exit.class) != null && goalMap.values().stream()
                        .map(Label::getText)
                        .filter(text -> !text.equalsIgnoreCase("Complete"))
                        .count() <= 1;
                if (onlyExitRemaining) {
                    goalMap.get(Exit.class).setText("Get to the exit!");
                }
            });
            goalMap.get(entry.getKey()).setText(String.valueOf(entry.getValue().get()));
            goalMap.get(entry.getKey()).setFont(new Font("Default", 5));
        });
        goal.getCompleteProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                display.addFinalText(completeText, "Level Completed!", "0x408140");
            }
        }));
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
        imageMap.put(Arrow.class, Map.of(
                DEFAULT_IMG, new Image((new File("images/arrow_directions/arrow_right.png")).toURI().toString()),
                Arrow.RIGHT, new Image((new File("images/arrow_directions/arrow_right.png")).toURI().toString()),
                Arrow.LEFT, new Image((new File("images/arrow_directions/arrow_left.png")).toURI().toString()),
                Arrow.UP, new Image((new File("images/arrow_directions/arrow_up.png")).toURI().toString()),
                Arrow.DOWN, new Image((new File("images/arrow_directions/arrow_down.png")).toURI().toString())
        ));
        imageMap.put(Dog.class, Map.of(
                DEFAULT_IMG, new Image((new File("images/hound.png")).toURI().toString())));
        return imageMap;
    }

}
