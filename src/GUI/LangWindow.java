package GUI;

import GUI.Controllers.LangController;
import Network.Connection.ClientCommandHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class LangWindow extends Pane {
    private Pane root;
    private Scene scene;

    public LangWindow(Main main) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/lang.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        LangController langController = loader.getController();
        langController.setMain(main);
        langController.init();

        scene = new Scene(root);
    }

    public Scene getScreen() {
        return scene;
    }
}
