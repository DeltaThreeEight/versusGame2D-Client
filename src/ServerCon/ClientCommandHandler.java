package ServerCon;

import Entities.Human;
import GUI.Main;
import GUI.MainWindow;
import Server.Command;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class ClientCommandHandler {

    static HashMap<String,Human> joinedPlayers = new HashMap<>();
    public static Human playerClient;
    static String authToken = null;
    public static ClientCommandHandler dH;
    public static MainWindow mainWindow;
    private Stage token_window;
    private ObjectOutputStream writer;
    private static boolean isAuth = false;

    public ClientCommandHandler(ObjectOutputStream writer) {
        this.writer = writer;
        dH = this;
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
        for (Node n : mainWindow.getMainController().getGraphics().getChildren()) {
            if (n instanceof Human) {
                ((Human) n).hide();
            }
        }
        ClientCommandHandler.setIsAuth(false);
        ClientCommandHandler.playerClient = null;
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
