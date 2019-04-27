package GUI;

import Entities.Human;
import Entities.Moves;
import GUI.Controllers.MainController;
import ServerCon.ClientCommandHandler;
import Server.Command;
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


        ClientCommandHandler.dH.executeCommand(new Command("show"));

        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (ClientCommandHandler.playerClient != null) {
                switch (keyCode.toString()) {
                    case "W":
                        ClientCommandHandler.dH.executeCommand(new Command("move", "BACK"));
                        ClientCommandHandler.playerClient.move(Moves.BACK);
                        break;
                    case "S":
                        ClientCommandHandler.dH.executeCommand(new Command("move", "FORWARD"));
                        ClientCommandHandler.playerClient.move(Moves.FORWARD);
                        break;
                    case "A":
                        ClientCommandHandler.dH.executeCommand(new Command("move", "LEFT"));
                        ClientCommandHandler.playerClient.move(Moves.LEFT);
                        break;
                    case "D":
                        ClientCommandHandler.dH.executeCommand(new Command("move", "RIGHT"));
                        ClientCommandHandler.playerClient.move(Moves.RIGHT);
                        break;
                }
                camera();
            }
        });

//        Pane graphics = mainController.getGraphics();
//        graphics.getChildren().addAll(imageView1);
    }

    public void camera(){
        Pane graphics = mainController.getGraphics();
        double WIDTH = graphics.getWidth();
        double HEIGHT = graphics.getHeight();
        double PLAYER_X = WIDTH/2-10;
        double PLAYER_Y = HEIGHT/2-10;

        Human plr = ClientCommandHandler.playerClient;
        plr.translateXProperty().addListener((obs , old , newValue)->{
            int offset = newValue.intValue();
            if (offset >  PLAYER_X && offset<WIDTH+200 - plr.getLocation().getX()){
                graphics.setLayoutX(-(offset - plr.getLocation().getX() ));
            }else if(offset< PLAYER_X && offset<WIDTH+200 - plr.getLocation().getX()){
                graphics.setLayoutX(-(offset - plr.getLocation().getX() ));
            }
        });
        plr.translateYProperty().addListener((obs , old , newValue)->{
            int offset = newValue.intValue();
            if(offset >  PLAYER_Y && offset<HEIGHT+200 - plr.getLocation().getY()){
                graphics.setLayoutY(-(offset - plr.getLocation().getY()));
            }else if(offset< PLAYER_Y && offset<HEIGHT+200 - plr.getLocation().getY()){
                graphics.setLayoutY(-(offset - plr.getLocation().getY()));
            }
        });
    }

    public MainController getMainController() {
        return mainController;
    }

    public Scene getScen() {
        return scene;
    }

}
