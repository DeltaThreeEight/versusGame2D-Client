package GUI;

import GUI.Controllers.LogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoginWindow extends AnchorPane {
    private AnchorPane root;
    private Scene scene;

    public LoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/log.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        ((LogController) loader.getController()).localize();
        scene = new Scene(root);
    }

    public Scene getScen() {
        return scene;
    }

}
