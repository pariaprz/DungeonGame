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

        JSONObject json = new JSONObject(new JSONTokener(new FileReader("dungeons/" + "advanced.json")));
        DungeonLoader dungeonLoader = new DungeonLoader(json);
        DungeonController controller = new DungeonController(dungeonLoader.load(), dungeonLoader.loadGoal());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("DungeonView.fxml"));
        loader.setController(controller.loadDungeonView());
        Parent root = loader.load();
        Scene scene = new Scene(root);
        System.out.println(getClass().getResource("fontstyle.css"));
        scene.getStylesheets().add(getClass().getResource("./fontstyle.css").toExternalForm());
        root.requestFocus();
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
