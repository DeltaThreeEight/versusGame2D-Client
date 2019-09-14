package Network.Connection;

import Entities.Human;
import Entities.Spy;
import GUI.Main;
import GUI.MainWindow;
import Server.Commands.ClientCommand;
import Resources.TextResources;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class ClientCommandHandler {

    private HashMap<String,Human> joinedPlayers = new HashMap<>();
    private Human playerClient;
    private MainWindow mainWindow;
    private Stage tokenWindow;
    private ObjectOutputStream writer;
    private ObjectInputStream input;
    private boolean isAuth = false;
    private Rectangle hpBar;
    private Rectangle hpRemaining;
    private Label hp;
    private Label ammo;
    private Label ammoAmount;
    private TextResources resources;
    private Main main;

    public ClientCommandHandler(Main main, ObjectInputStream input, ObjectOutputStream writer) {
        this.writer = writer;
        this.input = input;
        this.main = main;
        this.resources = main.getTextResources();
    }

    public HashMap<String, Human> getJoinedPlayers() {
        return joinedPlayers;
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public Main getMain() {
        return main;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public Rectangle getHpBar() {
        return hpRemaining;
    }

    public void setPlayerClient(Human playerClient) {
        this.playerClient = playerClient;
        double maxHealth;

        if (playerClient instanceof Spy)
            maxHealth = 100;
        else maxHealth = 150;

        Pane graphics = mainWindow.getMainController().getGraphics();

        hp = new Label("HP");
        hp.setTextFill(Paint.valueOf("RED"));

        ammo = new Label("AMMO:");
        ammo.setTranslateX(90);

        ammoAmount = new Label();
        ammoAmount.setTranslateX(135);
        ammoAmount.setText(playerClient.getAmmo()+"");

        hpBar = new Rectangle();

        hpBar.setFill(Paint.valueOf("BLACK"));
        hpBar.setTranslateX(20);
        hpBar.setWidth(60);
        hpBar.setHeight(16);

        hpRemaining = new Rectangle();

        hpRemaining.setFill(Paint.valueOf("RED"));
        hpRemaining.setHeight(12);
        hpRemaining.setWidth(56.0*(((double)playerClient.getHealth())/maxHealth));
        hpRemaining.setTranslateX(22);
        hpRemaining.setTranslateY(2);

        graphics.getChildren().addAll(hp, hpBar, hpRemaining, ammo, ammoAmount);
    }

    public Human getPlayerClient() {
        return playerClient;
    }

    public void setPlayerNull() {
        Pane graphics = mainWindow.getMainController().getGraphics();

        playerClient.hide();
        playerClient = null;

        graphics.getChildren().removeAll(hp, hpBar, hpRemaining, ammo, ammoAmount);
    }

    public Label getAmmoAmount() {
        return ammoAmount;
    }

    public Stage getTokenWindow() {
        return tokenWindow;
    }

    public void setTokenWindow(Stage tokenWindow) {
        this.tokenWindow = tokenWindow;
    }

    boolean getIsAuth() {
        return isAuth;
    }

    public void executeCMD(ClientCommand cmd) {
        switch (cmd.getCommandName()) {
            case "login":
                login(cmd.getArg(0), cmd.getArg(1));
                break;
            case "register":
                register(cmd.getArg(0), cmd.getArg(1), cmd.getArg(2), cmd.getArg(3));
                break;
            case "deauth":
                sendCMD(cmd);
                deauth();
                break;
            default:
                sendCMD(cmd);
                break;
        }
    }

    private void login(String login, String password) {
        if (login.trim().length() == 0) {
            main.showAlert(resources.LOGIN_EMPTY);
            return;
        }

        if (password.length() < 6) {
            main.showAlert(resources.PASS_SHORT);
            return;
        }

        sendCMD(new ClientCommand("login", login.trim(), password));
    }

    private void register(String login, String email, String password, String passwordRepeat) {
        if (login.trim().length() == 0) {
            main.showAlert(resources.LOGIN_EMPTY);
            return;
        }
        if (password.length() < 6) {
            main.showAlert(resources.PASS_SHORT);
            return;
        } else if (!password.equals(passwordRepeat)) {
            main.showAlert(resources.PASS_NOT_EQUALS);
            return;
        }
        if (email.trim().length() == 0) {
            main.showAlert(resources.EMAIL_EMPTY);
            return;
        }

        sendCMD(new ClientCommand("register", login.trim(), password, email.trim()));
    }

    public void sendCMD(ClientCommand cmd) {
        try {
            writer.writeObject(cmd);
            writer.flush();
        } catch (IOException e) {
            System.err.println("Невозможно отравить запрос серверу.");
            e.printStackTrace();
        }
    }

    public void deauth() {
        for (Human n : joinedPlayers.values()) {
            n.hide();
        }

        setIsAuth(false);
        joinedPlayers.clear();
    }


    public void setIsAuth(boolean a) {
        isAuth = a;
    }

}
