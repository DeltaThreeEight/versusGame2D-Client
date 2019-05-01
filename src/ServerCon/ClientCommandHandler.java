package ServerCon;

import Entities.Human;
import Entities.Spy;
import GUI.Main;
import GUI.MainWindow;
import Server.Command;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class ClientCommandHandler {

    static HashMap<String,Human> joinedPlayers = new HashMap<>();
    private static Human playerClient;
    static String authToken = null;
    public static ClientCommandHandler dH;
    public static MainWindow mainWindow;
    private Stage token_window;
    private ObjectOutputStream writer;
    private static boolean isAuth = false;
    private static Rectangle hpBar;
    private static Rectangle hpRem;
    private static Label hp;

    public ClientCommandHandler(ObjectOutputStream writer) {
        this.writer = writer;
        dH = this;
    }

    public static Rectangle getHpBar() {
        return hpRem;
    }

    public static void setPlayerClient(Human playerClient) {
        Pane graphics = mainWindow.getMainController().getGraphics();
        hp = new Label("HP");
        hp.setTextFill(Paint.valueOf("RED"));
        hpBar = new Rectangle();
        hpRem = new Rectangle();
        hpRem.setHeight(12);
        hpRem.setWidth(56);
        hpRem.setTranslateX(22);
        hpRem.setTranslateY(2);
        hpBar.setTranslateX(20);
        hpBar.setWidth(60);
        hpBar.setHeight(16);
        hpRem.setFill(Paint.valueOf("RED"));
        hpBar.setFill(Paint.valueOf("BLACK"));
        double maxHealt;
        if (playerClient instanceof Spy)
            maxHealt = 100;
        else maxHealt = 150;
        hpRem.setWidth(56.0*(((double)playerClient.getHealth())/maxHealt));
        graphics.getChildren().addAll(hp, hpBar, hpRem);
        ClientCommandHandler.playerClient = playerClient;
    }

    public static Human getPlayerClient() {
        return playerClient;
    }

    public static void setPlayerNull() {
        Pane graphics = mainWindow.getMainController().getGraphics();
        playerClient.hide();
        playerClient = null;
        graphics.getChildren().removeAll(hp, hpBar, hpRem);
    }

    public Stage getToken_window() {
        return token_window;
    }

    public void setToken_window(Stage token_window) {
        this.token_window = token_window;
    }

    public static boolean getIsAuth() {
        return isAuth;
    }

    public boolean executeCommand(Command cmd) {
        try {
            cmd.setToken(authToken);
            switch (cmd.getName()) {
                default:
                    sendCMD(cmd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void sendCMD(Command cmd) {
        try {
            writer.writeObject(cmd);
            writer.flush();
        } catch (IOException e) {
            System.err.println("Невозможно отравить запрос серверу.");
            e.printStackTrace();
        }
    }


    public static void setIsAuth(boolean a) {
        isAuth = a;
    }

    public void deauth() {
        for (Human n : joinedPlayers.values()) {
            n.hide();
        }
        ClientCommandHandler.setIsAuth(false);
        ClientCommandHandler.joinedPlayers.clear();
    }

    public void sendHuman(Human hum) {
        try {
            writer.writeObject(hum);
            writer.flush();
        } catch (IOException e) {
            System.err.println("Невозможно отравить персонажа");
            e.printStackTrace();
        }
    }

}
