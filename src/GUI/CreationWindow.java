package GUI;

import GUI.Controllers.CreationController;
import GUI.Controllers.TokenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CreationWindow {
    private AnchorPane root;
    private Scene scene;

    public CreationWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/creation.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        ((CreationController) loader.getController()).localize();
        scene = new Scene(root);
    }

    public Scene getScen() {
        return scene;
    }
}
