package GUI;

import Entities.*;
import GUI.Controllers.MainController;
import GUI.Controllers.MapController;
import Server.Commands.ClientCommand;
import Network.Connection.ClientCommandHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;


public class MainWindow extends AnchorPane {
    private AnchorPane root;
    private Scene scene;
    private Pane map;
    private Pane house_floor;

    private MainController mainController;

    public MainWindow(ClientCommandHandler handler) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));
        try {
            root = loader.load();
            mainController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        handler.getMain().setMainController(mainController);

        scene = new Scene(root);

        mainController.setHandler(handler);
        mainController.localize();

        handler.executeCMD(new ClientCommand("show"));

        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (handler.getPlayerClient() != null) {
                switch (keyCode) {
                    case W:
                        handler.getPlayerClient().move(Moves.BACK);
                        break;
                    case S:
                        handler.getPlayerClient().move(Moves.FORWARD);
                        break;
                    case A:
                        handler.getPlayerClient().move(Moves.LEFT);
                        break;
                    case D:
                        handler.getPlayerClient().move(Moves.RIGHT);
                        break;
                    case F:
                        handler.getPlayerClient().shoot();
                        break;
                }
            }
        });

        Pane graphics = mainController.getGraphics();
        loadMap(graphics);
    }

    public MainController getMainController() {
        return mainController;
    }

    public Scene getScreen() {
        return scene;
    }

    private void loadMap(Pane graphics) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Map.fxml"));
        try {
            map = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        MapController mapController = loader.getController();

        house_floor = mapController.getHouseFloor();
        ArrayList<BigWall> walls = mapController.getAllWalls();

        graphics.getChildren().addAll(map);
        graphics.getChildren().addAll(walls);
        graphics.setStyle("-fx-background-color: green;");
    }

    public Pane getMap() {
        return map;
    }

    public Pane getHouseFloor() {
        return house_floor;
    }
}
