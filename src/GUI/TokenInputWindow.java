package GUI;

import GUI.Controllers.TokenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TokenInputWindow {
    private AnchorPane root;
    private Scene scene;

    public TokenInputWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/token.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        ((TokenController) loader.getController()).localize();
        scene = new Scene(root);
    }

    public Scene getScen() {
        return scene;
    }
}
