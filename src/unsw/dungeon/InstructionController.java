package unsw.dungeon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import unsw.dungeon.DungeonView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;

public class InstructionController {
    
    @FXML
    private Button Return;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void handleReturn(ActionEvent press){
        stage.setScene(scene);
        stage.show();
        root.requestFocus();
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setScene(Scene scene){
        this.scene = scene;
    }
    
    public void setRoot(Parent root){
        this.root = root;
    }
}