package GUI;

import Entities.*;
import GUI.Controllers.MainController;
import GUI.Controllers.MapController;
import ServerCon.ClientCommandHandler;
import Server.Command;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;


public class MainWindow extends AnchorPane {
    private AnchorPane root;
    private Scene scene;

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


        ClientCommandHandler.dH.executeCommand(new Command("show"));

        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (ClientCommandHandler.getPlayerClient() != null) {
                switch (keyCode) {
                    case W:
                        ClientCommandHandler.getPlayerClient().move(Moves.BACK);
                        break;
                    case S:
                        ClientCommandHandler.getPlayerClient().move(Moves.FORWARD);
                        break;
                    case A:
                        ClientCommandHandler.getPlayerClient().move(Moves.LEFT);
                        break;
                    case D:
                        ClientCommandHandler.getPlayerClient().move(Moves.RIGHT);
                        break;
                    case F:
                        ClientCommandHandler.getPlayerClient().shoot();
                        break;
                }
            }
        });


        Pane graphics = mainController.getGraphics();
        loadMap(graphics);
    }

    public void camera(){
        Pane graphics = mainController.getGraphics();
        double WIDTH = graphics.getWidth();
        double HEIGHT = graphics.getHeight();
        double PLAYER_X = WIDTH/2-10;
        double PLAYER_Y = HEIGHT/2-10;

        Human plr = ClientCommandHandler.getPlayerClient();
        plr.translateXProperty().addListener( (obs , old , newValue) ->
            {
//                int offset = newValue.intValue();
//                if (offset >  PLAYER_X && offset < WIDTH+200 - plr.getLocation().getX()) {
//                    plr.setLayoutX(-(offset - plr.getLocation().getX() ));
//                } else if (offset< PLAYER_X && offset<WIDTH+200 - plr.getLocation().getX()) {
//                    plr.setLayoutX(-(offset - plr.getLocation().getX() ));
//                }
            });
        plr.translateYProperty().addListener( (obs , old , newValue) ->
        {
//            int offset = newValue.intValue();
//            if (offset >  PLAYER_Y && offset < HEIGHT+200 - plr.getLocation().getY()) {
//                graphics.setLayoutY(-(offset - plr.getLocation().getY()));
//            } else if (offset< PLAYER_Y && offset<HEIGHT+200 - plr.getLocation().getY()) {
//                graphics.setLayoutY(-(offset - plr.getLocation().getY()));
//            }
        });
    }

    public MainController getMainController() {
        return mainController;
    }

    public Scene getScen() {
        return scene;
    }

    private void loadMap(Pane graphics) {
        Pane root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Map.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        ArrayList<BigWall> a = ((MapController) loader.getController()).getAllWalls();

        graphics.getChildren().addAll(root);
        graphics.getChildren().addAll(a);
        graphics.setStyle("-fx-background-color: green;");
    }

}
