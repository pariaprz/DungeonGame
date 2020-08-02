package unsw.dungeon;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unsw.dungeon.InstructionController;

/**
 * A controller for the dungeon.
 * It is responsible for managing entities,
 * publishing entity changes through entity wrappers to the view, or other subscribers,
 * and it is responsible for listening to goal completion.
 */
public class InstructionScreen {
    private Stage stage;
    private String title;
    private InstructionController controller;
    private Scene scene;

    public InstructionScreen(Stage stage) throws IOException{
        this.stage = stage;
        this.title = "Instructions";
        controller = new InstructionController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Instructions.fxml"));
        loader.setController(controller);

        Parent root = loader.load();
        scene = new Scene(root);
        root.requestFocus();
    }

    public void start(){
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public InstructionController getInstructionController(){
        return controller;
    }
}