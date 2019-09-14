package GUI;

import GUI.Controllers.RegController;
import Network.Connection.ClientCommandHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class RegistrationWindow {
    private AnchorPane root;
    private Scene scene;

    public RegistrationWindow(ClientCommandHandler handler) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/reg.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        RegController regController = loader.getController();
        regController.setHandler(handler);
        regController.localize();

        scene = new Scene(root);
    }
    public Scene getScreen() {
        return scene;
    }

}
