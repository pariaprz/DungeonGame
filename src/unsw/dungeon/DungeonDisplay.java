package unsw.dungeon;

import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * A DungeonLoader that also creates the necessary ImageViews for the UI,
 * connects them via listeners to the model, and creates a controller.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonDisplay {
    private final Map<Class<? extends Entity>, Map<String, Image>> imageMap;
    private int height, width;
    private GridPane mainMenu;

    private GridPane gridpane;

    public DungeonDisplay(Map<Class<? extends Entity>, Map<String, Image>> imageMap)  {
        this.imageMap = imageMap;
    }

    public void initialiseDungeon(int width, int height, List<ImageView> entities, Map<Class<? extends Entity>, Label> goals) {
        this.height = height;
        this.width = width;
        gridpane.getChildren().clear();

        Image ground = new Image((new File("images/dirt_0_new.png")).toURI().toString());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                gridpane.add(new ImageView(ground), x, y);
            }
        }

        for (ImageView entity : entities) {
            gridpane.getChildren().add(entity);
        }

        int[] xPosition = {0};
        goals.forEach((entity, label) -> {
        if (entity == Exit.class) return;
            gridpane.add(new ImageView(imageMap.get(entity).get(DungeonView.DEFAULT_IMG)), xPosition[0]++, height);
            gridpane.add(label, xPosition[0], height, 2, 1);
            xPosition[0] += 2;
        });
        if (goals.get(Exit.class) != null) {
            gridpane.add(new ImageView(imageMap.get(Exit.class).get(DungeonView.DEFAULT_IMG)), xPosition[0]++, height);
            gridpane.add(goals.get(Exit.class), xPosition[0], height, 5, 1);
        }

        initialisePlayerStateTimer();
    }

    private void initialisePlayerStateTimer() {
        PlayerState.stateName.addListener((observableValue, oldState, newState) -> {
            gridpane.getChildren().removeIf(n -> GridPane.getRowIndex(n) == height && GridPane.getColumnIndex(n) == width-1);
            if (newState.equals(InvinciblePlayerState.STATE_NAME)) {
                gridpane.add(new ImageView(imageMap.get(Invincibility.class).get(DungeonView.DEFAULT_IMG)), width-1, height);
            } else if (newState.equals(WallWalkerPlayerState.STATE_NAME)) {
                gridpane.add(new ImageView(imageMap.get(WallWalker.class).get(DungeonView.DEFAULT_IMG)), width-1, height);
            } else {
                System.out.println("Switching to default.");
            }
        });
        PlayerState.timeRemaining.addListener((observableValue, oldTime, newTime) -> {
            gridpane.getChildren().removeIf(n -> GridPane.getRowIndex(n) == height
                    && GridPane.getColumnIndex(n) == width-1 && n instanceof Text);
            Text text = new Text(String.valueOf(newTime));
            text.setId("state-timer");
            gridpane.add(text, width-1, height);
        });
    }

    public void showMainMenu(GridPane mainMenu, List<Pair<String, String>> levels,
                             Function<String, Boolean> clickHandler) {
        if (this.mainMenu == null) {
            this.mainMenu = mainMenu;
        }
        mainMenu.setVisible(true);
        TextFlow levelList = (TextFlow)mainMenu.lookup("#level-list");
        levelList.getChildren().clear();
        levelList.getChildren().add(new LineBreak());
        gridpane.getChildren().clear();

        levels.forEach((level) -> {
            Text newLevel = new Text(level.getKey());
            newLevel.setOnMouseClicked((event) -> {
                if (clickHandler.apply(level.getValue())) {
                    mainMenu.setVisible(false);
                }
            });
            levelList.getChildren().addAll(newLevel, new LineBreak());
        });
    }

    public void setGridpane(GridPane gridpane) {
        this.gridpane = gridpane;
    }

    public void initialiseInventory(PlayerInventory inventory) {
        IntStream.range(0, height).forEach(i -> {
            Rectangle r = new Rectangle(32, 32);
            r.setStrokeType(StrokeType.INSIDE);
            r.setFill(Color.web("0xEAD0A8"));
            gridpane.add(r, width, i);
            Rectangle r2 = new Rectangle(32, 32);
            r2.setFill(Color.web("0xEAD0A8"));
            gridpane.add(r2, width+1, i);
        });

        inventory.getUsableIndex().addListener(((observableValue, oldInt, newInt) -> {
            // TODO: add indicator.
            if ((int)oldInt != PlayerInventory.UNEQUIPPED) {
                ((Rectangle)gridpane.getChildren()
                        .filtered(n ->
                        GridPane.getColumnIndex(n) == width &&
                                GridPane.getRowIndex(n) == height-1-(int)oldInt && n instanceof Rectangle
                        ).get(0)).setFill(Color.web("0xEAD0A8"));
            }
            if ((int)newInt != PlayerInventory.UNEQUIPPED) {
                ((Rectangle)gridpane.getChildren()
                        .filtered(n ->
                                GridPane.getColumnIndex(n) == width &&
                                        GridPane.getRowIndex(n) == height-1-(int)newInt && n instanceof Rectangle
                        ).get(0)).setFill(Color.web("0xFFCF40"));
            }
        }));

        inventory.getItems().addListener(((observableValue, oldList, newList) -> {
            gridpane.getChildren().removeIf(n -> GridPane.getColumnIndex(n) >= width && !(n instanceof Rectangle));
            FilteredList<Pair<Collectable, Integer>> usable = newList.filtered(p -> p.getKey().canUse(inventory));
            FilteredList<Pair<Collectable, Integer>> unUsable = newList.filtered(p -> !p.getKey().canUse(inventory));
            if (usable.isEmpty() && unUsable.isEmpty()) {
                gridpane.add(new ImageView(), width, 0);
                gridpane.add(new ImageView(), width+1, 0);
            }
            IntStream.range(0, usable.size()).forEach(i -> {
                ImageView newView = new ImageView(imageMap.get(usable.get(i).getKey().getClass()).get(DungeonView.DEFAULT_IMG));
                gridpane.add(newView, width, height-1-i);
                Label label = new Label(usable.get(i).getValue().toString());
                label.setPadding(new Insets(0, 0, 0, 12));
                label.setMinWidth(32);
                gridpane.add(label, width+1, height-1-i);
            });

            IntStream.range(0, unUsable.size()).forEach(i -> {
                ImageView newView = new ImageView(imageMap.get(unUsable.get(i).getKey().getClass()).get(DungeonView.DEFAULT_IMG));
                gridpane.add(newView, width, i);
                Label label = new Label(unUsable.get(i).getValue().toString());
                label.setPadding(new Insets(0, 0, 0, 12));
                label.setMinWidth(32);
                gridpane.add(label, width+1, i);
            });
        }));
    }

    public void addGoalIcons(FlowPane goalIcons, Map<Class<? extends Entity>, Label> goals) {
        goalIcons.getChildren().clear();
        for (Class<? extends Entity> entity : goals.keySet()) {
            goalIcons.getChildren().add(new ImageView(imageMap.get(entity).get(DungeonView.DEFAULT_IMG)));
        }
    }

    public void setGoalString(TextFlow goalList, Goal goal) {
        goalList.getChildren().clear();
        String result = createGoalString(goal);
        String[] resultGoals = result.split("!");
        int indent = 0;
        for (int i = 0; i < resultGoals.length; i++) {
            if (resultGoals[i].contains("(")) {
                indent += 1;
            }
            String textToAdd = resultGoals[i].replaceAll("[()]", "");
            if (textToAdd.length() > 1) {
                Text newText = new Text("- " + textToAdd);
                newText.setTranslateX(16*indent);
                goalList.getChildren().add(newText);
                goalList.getChildren().add(new LineBreak());
            }
            if (resultGoals[i].contains(")")) {
                indent -= 1;
            }
        }
    }

    public String createGoalString(Goal goal) {
        StringBuilder result = new StringBuilder("(");
        if (goal.getGoalEngine() instanceof ComplexGoalEngine) {
            int i = 0;
            for (Goal subgoal : ((ComplexGoalEngine) goal.getGoalEngine()).getChildGoals()) {
                result.append(createGoalString(subgoal));
                if (i < ((ComplexGoalEngine) goal.getGoalEngine()).getChildGoals().size()-1) {
                    if (goal.getGoalEngine() instanceof ANDGoalEngine) {
                        result.append(" AND! ");
                    } else {
                        result.append(" OR! ");
                    }
                }
                i++;
            }
        } else if (goal.getGoalEngine() instanceof TreasureGoalEngine) {
            return "Collect ALL the treasure!";
        } else if (goal.getGoalEngine() instanceof EnemyGoalEngine) {
            return "Slay ALL the enemies!";
        } else if (goal.getGoalEngine() instanceof SwitchesGoalEngine) {
            return "Place a boulder on ALL the switches!";
        } else if (goal.getGoalEngine() instanceof ExitGoalEngine) {
            return "Get to the exit!";
        }

        result.append(")");
        return result.toString();
    }

    public void addFinalText(Text completeText, String text, String color) {
        completeText.setText(text);
        completeText.setFont(new Font("VT323", 40));
        completeText.setFill(Color.web(color));
    }

    public void updateSquares(ImageView newEntity){
        gridpane.getChildren().add(newEntity);
    }
}
