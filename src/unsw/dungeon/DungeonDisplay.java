package unsw.dungeon;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.*;

/**
 * A DungeonLoader that also creates the necessary ImageViews for the UI,
 * connects them via listeners to the model, and creates a controller.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonDisplay {
    private static String DEFAULT_IMG = "DEFAULT_IMAGE";

    private final Map<Class<? extends Entity>, Map<String, Image>> imageMap;
    private final int height, width;
    private final Node menu;
    boolean isDisplayed = false;

    private GridPane gridpane;

    public DungeonDisplay(int height, int width, Map<Class<? extends Entity>, Map<String, Image>> imageMap)  {
        this.height = height;
        this.width = width;
        this.imageMap = imageMap;
        this.menu = createMenu();
    }

    public void initialize(List<ImageView> entities, Map<Class<? extends Entity>, Label> goals) {
        Image ground = new Image((new File("images/dirt_0_new.png")).toURI().toString());
        Image inv_1sec = new Image((new File("images/Invincibility_Time_Icons/invincibility_1sec.png")).toURI().toString());
        gridpane.add(menu, 0, 0, width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                gridpane.add(new ImageView(ground), x, y);
            }
        }

        for (ImageView entity : entities) {
            gridpane.getChildren().add(entity);
        }

        int[] xPosition = { 0 };
        int yPosition = height+1;
        goals.forEach((entity, label) -> {
            if (entity == Exit.class) return;
            gridpane.add(new ImageView(imageMap.get(entity).get(DEFAULT_IMG)), xPosition[0]++, yPosition);
            gridpane.add(label, xPosition[0], yPosition, 2, 1);
            xPosition[0] += 2;
        });
        if (goals.get(Exit.class) != null) {
            gridpane.add(new ImageView(imageMap.get(Exit.class).get(DEFAULT_IMG)), xPosition[0]++, yPosition);
            gridpane.add(goals.get(Exit.class), xPosition[0], yPosition, 5, 1);
        }

        gridpane.add(new ImageView(inv_1sec), 4, 0);
    }

    public void setGridpane(GridPane gridpane) {
        this.gridpane = gridpane;
    }

    public void toggleMenu() {
        if (isDisplayed) menu.toBack();
        else menu.toFront();
        isDisplayed = !isDisplayed;
    }

    public void addFinalText(Text completeText, String text, String color) {
        completeText.setText(text);
        completeText.setFont(new Font("VT323", 40));
        completeText.setFill(Color.web(color));
    }

    public void updateSquares(ImageView newEntity){
        gridpane.getChildren().add(newEntity);
    }

    private Node createMenu() {
        int width = this.width*32;
        int height = this.height*32;
        Pane pane = new Pane();
        Rectangle background = new Rectangle(width, height);
        background.setFill(Color.web("0xEAD0A8"));

        Label title = new Label("Dungeon Hunter");
        title.setTextFill(Color.web("0xEAD0A8"));
        title.setLayoutX(width/2.0 - 170);
        title.setLayoutY(64.0);
        title.setId("hunter");

        Text goal = new Text("Goal");
        goal.setFill(Color.web("0x76552B"));
        goal.setLayoutX(width/2.0 - 170);
        goal.setLayoutY(64.0*3);
        goal.setId("goal");


        pane.getChildren().addAll(background, title, goal);
        return pane;
    }

}
