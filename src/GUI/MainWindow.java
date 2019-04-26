package GUI;

import GUI.Controllers.MainController;
import ServerCon.ClientCommandHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;


public class MainWindow extends AnchorPane {
    private AnchorPane root;
    private Scene scene;
    private Pane graphics = new Pane();

    private Image image1 = new Image(getClass().getResourceAsStream("map.png"));
    private ImageView imageView1 = new ImageView(image1);
    private MainController mainController;

    public MainWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));
        try {
            root = loader.load();
            mainController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        scene = new Scene(root);
        mainController.localize();


        ClientCommandHandler.dH.executeCommand("show");

        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            switch (keyCode.toString()) {
                case "W":
                    ClientCommandHandler.dH.executeCommand("move FORWARD");
                    break;
                case "S":
                    ClientCommandHandler.dH.executeCommand("move BACK");
                    break;
                case "A":
                    ClientCommandHandler.dH.executeCommand("move LEFT");
                    break;
                case "D":
                    ClientCommandHandler.dH.executeCommand("move RIGHT");
                    break;
            }
        });

//        Pane graphics = mainController.getGraphics();
//        graphics.getChildren().addAll(imageView1);
    }

    public MainController getMainController() {
        return mainController;
    }

    public Scene getScen() {
        return scene;
    }

}
