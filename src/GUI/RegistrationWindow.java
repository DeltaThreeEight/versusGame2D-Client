package GUI;

import GUI.Controllers.RegController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class RegistrationWindow extends AnchorPane {
    private AnchorPane root;
    private Scene scene;
    private FXMLLoader loader;

    public RegistrationWindow() {
        loader = new FXMLLoader(getClass().getResource("fxml/reg.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        ((RegController) loader.getController()).localize();
        scene = new Scene(root);
    }
    public Scene getScen() {
        return scene;
    }

}
