package unsw.dungeon;

import java.io.FileReader;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DungeonApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Dungeon");

        DungeonController controller = new DungeonController();
        InstructionScreen instrScreen = new InstructionScreen(primaryStage);
        controller.setInstructionScreen(instrScreen);

        instrScreen.getInstructionController().setStage(primaryStage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("DungeonView.fxml"));
        loader.setController(controller.loadDungeonView());



        Parent root = loader.load();
        Scene scene = new Scene(root);
        instrScreen.getInstructionController().setScene(scene);
        instrScreen.getInstructionController().setRoot(root);
        
        scene.getStylesheets().add(getClass().getResource("./style.css").toExternalForm());
        root.requestFocus();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
