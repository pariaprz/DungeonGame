package unsw.dungeon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import unsw.dungeon.DungeonView;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class InstructionController {
    
    @FXML
    private Button Return;
    private Stage stage;
    private Scene scene;

    @FXML
    public void handleReturn(ActionEvent press){
        stage.setScene(scene);
        stage.show();
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setScene(Scene scene){
        this.scene = scene;
    }
}