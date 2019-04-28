package GUI;

import GUI.Controllers.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SampleWindow {
    private Pane root;
    private Scene scene;

    public SampleWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/sample.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        ((Controller) loader.getController()).localize();
        scene = new Scene(root);

    }

    public Scene getScen() {
        return scene;
    }
}
