package GUI;
import Server.Command;
import ServerCon.ClientCommandHandler;
import ServerCon.ClientReciever;
import javafx.application.Application;
import javafx.application.Platform;
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
    private static Main main = null;
    private String host = "127.0.0.1";
    private int port = 8901;
    private ResourceBundle rb;
    private Locale locale;

    public Main(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public Main(int port) {
        this.port = port;
    }

    public Main() { }

    public void start(Stage stages) {
        this.port = main.port;
        this.host = main.host;
        main = this;
        primaryStage = stages;

        primaryStage.setTitle("Lab 8");

        primaryStage.setScene(new LangWindow().getScen());
        primaryStage.show();

    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void tryConnect() {

        Label connecting = new Label(String.format("Соединение с %s:%s...", host, port));

        Parent root = new FlowPane(connecting);
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        primaryStage.show();

        Socket socket = null;

        try {
            socket = new Socket(host, port);
            connecting.setText(String.format("Соединение успешно уставлено с %s:%s", host, port));
        } catch (Exception e) {
            connecting.setText(String.format("Не удалось установить соединение с %s:%s", host, port));
        }


        OutputStream out = null;
        InputStream iStream = null;

        try {
            out = socket.getOutputStream();
            iStream = socket.getInputStream();
        } catch (IOException e) {
            System.out.println("Невозможно получить поток вывода!");
            System.exit(-1);
        }


        ObjectOutputStream writer = null;
        ObjectInputStream inStream = null;

        try {
            writer = new ObjectOutputStream(out);
            inStream = new ObjectInputStream(iStream);
        } catch (IOException e) {
            System.out.println("Какие-то проблемы на стороне сервера");
            System.exit(0);
        }

        ClientReciever clientReciever = new ClientReciever(inStream);
        clientReciever.start();

        new ClientCommandHandler(writer);

        primaryScene = new SampleWindow().getScen();
        primaryStage.setScene(primaryScene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> System.exit(0));
    }

    public Scene getPrimaryScene() {
        return primaryScene;
    }

    public static void main(String args[]) {
        try {
            if (args.length == 1) main = new Main(Integer.parseInt(args[0]));
                else if (args.length > 1 ) main = new Main(Integer.parseInt(args[0]), args[1]);
                    else main = new Main();
        } catch (Exception e) {
            System.out.println("Неверно задан порт/хост");
            System.exit(-1);
        }

        launch(args);

    }

    public ResourceBundle getRb() {
        return rb;
    }

    public static Main getMain() {
        return main;
    }

    public void setLocale(String loc) {
        switch (loc) {
            case "Russian":
                locale = new Locale("ru", "RU");
                break;
            case "English":
                locale = new Locale("en", "US");
                break;
        }
        rb = ResourceBundle.getBundle("text", locale);
    }

    public static void showAlert(String text) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Information");
        dialog.setHeaderText(text);
        if (text.trim().equals("Авторизация успешна!") || text.trim().equals("Authorization successful!")
                || text.trim().equals("Почта подтверждена!") || text.trim().equals("Email is confirmed!"))
            showMainScreen();
        dialog.show();
    }

    private static void showMainScreen() {
        ClientCommandHandler.mainWindow = new MainWindow();
        getMain().getPrimaryStage().setScene(ClientCommandHandler.mainWindow.getScen());
        getMain().getPrimaryStage().setOnCloseRequest(event -> {
            getMain().getPrimaryStage().setScene(getMain().getPrimaryScene());
            ClientCommandHandler.dH.executeCommand(new Command("deauth"));
            ClientCommandHandler.dH.deauth();
            getMain().getPrimaryStage().setOnCloseRequest(event1 -> System.exit(0));
            Platform.runLater(() -> getMain().getPrimaryStage().show());
        });
    }

}
