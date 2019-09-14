package GUI;

import GUI.Controllers.LogRegController;
import Network.Connection.ClientCommandHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class LogRegWindow {
    private Pane root;
    private Scene scene;

    public LogRegWindow(ClientCommandHandler handler) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/sample.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        LogRegController controller = loader.getController();
        controller.setHandler(handler);
        controller.localize();

        scene = new Scene(root);
    }

    public Scene getScen() {
        return scene;
    }
}
