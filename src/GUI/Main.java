package GUI;
import GUI.Controllers.MainController;
import Server.Commands.ClientCommand;
import Network.Connection.ClientCommandHandler;
import Network.Connection.ClientReceiver;
import Resources.TextResources;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.Socket;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    private Stage primaryStage;
    private Scene primaryScene;
    private String host = "127.0.0.1";
    private int port = 21326;
    private TextResources resources;
    private Locale locale;
    private ClientCommandHandler handler;
    private MainController mainController;

    public Main(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public Main(int port) {
        this.port = port;
    }

    public Main() { }

    public Locale getLocale() {
        return locale;
    }

    public void start(Stage stages) {
        primaryStage = stages;

        primaryStage.setTitle("Lab 8");
        primaryStage.setResizable(false);

        primaryStage.setScene(new LangWindow(this).getScreen());
        primaryStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String args[]) {
        try {
            if (args.length == 1)
                new Main(Integer.parseInt(args[0]));
            else if (args.length > 1 )
                new Main(Integer.parseInt(args[0]), args[1]);
            else
                new Main();
        } catch (Exception e) {
            System.out.println("Неверно задан порт/хост");
            System.exit(-1);
        }

        launch(args);
    }

    public void tryConnect() {
        Label connecting = new Label(String.format("Connecting to %s:%s...", host, port));

        try {
            Parent root = new FlowPane(connecting);
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.show();

            Socket socket = new Socket(host, port);

            connecting.setText(String.format("Successfully connected to %s:%s", host, port));

            ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());

            handler = new ClientCommandHandler(this, reader, writer);

            Thread receiverThread = new Thread(new ClientReceiver(reader, handler, resources), "Receiver");
            receiverThread.start();

            primaryScene = new LogRegWindow(handler).getScen();
            primaryStage.setScene(primaryScene);
            primaryStage.show();

            primaryStage.setOnCloseRequest(event -> {
                handler.executeCMD(new ClientCommand("exit"));
                System.exit(0);
            });
        } catch (Exception e) {
            primaryStage.setResizable(true);
            primaryStage.setMinHeight(100);
            primaryStage.setMinWidth(600);
            primaryStage.setResizable(false);

            connecting.setText(String.format("Unable connect to %s:%s - %s", host, port, e.getMessage()));
            e.printStackTrace();
        }
    }

    public Scene getPrimaryScene() {
        return primaryScene;
    }

    public MainController getMainController() {
        return mainController;
    }

    public TextResources getTextResources() {
        return resources;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setLocale(String loc) {
        switch (loc) {
            case "Russian":
                locale = new Locale("ru", "RU");
                break;
            case "English":
                locale = new Locale("en", "US");
                break;
            case "Japanese":
                locale = new Locale("ja", "JP");
                break;
        }
        try {
            resources = new TextResources(ResourceBundle.getBundle("text", locale));
        } catch (Exception e) {
            Label noResources = new Label("Couldn't find resources file");
            Scene scene = new Scene(new FlowPane(noResources));

            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.setMinHeight(100);
            primaryStage.setMinWidth(600);
            primaryStage.setResizable(false);
            primaryStage.showAndWait();
        }
    }

    public void showAlert(String text) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);

        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle(resources.INFORMATION);
        dialog.setHeaderText(text);

        if (text.trim().equals(resources.AUTH_SUCCESS) || text.trim().equals(resources.EMAIL_CONF))
            showMainScreen(handler);

        dialog.show();
    }

    private void showMainScreen(ClientCommandHandler handler) {
        MainWindow mainWindow = new MainWindow(handler);
        handler.setMainWindow(mainWindow);

        handler.setIsAuth(true);

        primaryStage.setScene(mainWindow.getScreen());
        primaryStage.setResizable(true);

        primaryStage.setOnCloseRequest(event -> {
            handler.executeCMD(new ClientCommand("deauth"));
            closeReq();
        });
    }

    public void closeReq() {
        primaryStage.setScene(getPrimaryScene());
        primaryStage.sizeToScene();
        primaryStage.setMinWidth(1);
        primaryStage.setMinHeight(1);

        handler.deauth();

        primaryStage.setOnCloseRequest(event1 -> {
            handler.executeCMD(new ClientCommand("exit"));
            System.exit(0);
        });

        Platform.runLater(() -> {
            primaryStage.setMaximized(false);
            primaryStage.setResizable(false);
            primaryStage.show();
        });
    }

}
