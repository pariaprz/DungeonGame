package unsw.dungeon;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
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

    private GridPane gridpane;

    public DungeonDisplay(int height, int width, Map<Class<? extends Entity>, Map<String, Image>> imageMap, GridPane gridpane)  {
        this.height = height;
        this.width = width;
        this.imageMap = imageMap;
        this.gridpane = gridpane;
    }

    public void initialize(List<ImageView> entities, Map<Class<? extends Entity>, Label> goals) {
        Image ground = new Image((new File("images/dirt_0_new.png")).toURI().toString());
        Image inv_1sec = new Image((new File("images/Invincibility_Time_Icons/invincibility_1sec.png")).toURI().toString());

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

    public void addFinalText(String text, String color) {
        Text completeText = new Text(text);
        completeText.setFont(new Font("Verdana", 40));
        completeText.setFill(Color.web(color));
        completeText.setTextAlignment(TextAlignment.CENTER);
        gridpane.add(completeText, Math.max(width/2 - 5, 0), Math.max(height/2-3, height/2), width, 2);
    }

    public void updateSquares(ImageView newEntity){
        gridpane.getChildren().add(newEntity);
    }

}
