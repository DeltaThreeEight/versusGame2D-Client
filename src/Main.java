import Entities.Human;
import GUI.MainWindow;
import ServerCon.ClientCommandHandler;
import ServerCon.ClientReciever;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Exchanger;

public class Main extends Application {

    private static Stage stage;

    private static String host = "127.0.0.1";
    private static int port = 8901;

    public void start(Stage stages) throws IOException{
        stage = stages;

        Label connecting = new Label(String.format("Соединение с %s:%s...", host, port));

        connecting.setFont(Font.font(15));

        FlowPane root = new FlowPane(connecting);
        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setTitle("Hello JavaFX");
        stage.setWidth(400);
        stage.setHeight(90);

        stage.show();

        Socket socket = null;

        try {
            socket = new Socket(host, port);
            connecting.setText(String.format("Соединение успешно уставлено с %s:%s", host, port));
        } catch (Exception e) {
            connecting.setText(String.format("Не удалось установить соединение с %s:%s", host, port));
            System.exit(-1);
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

        Exchanger<String> exchanger = new Exchanger<>();
        ClientReciever clientReciever = new ClientReciever(inStream, exchanger);
        clientReciever.start();

        ClientCommandHandler cmdHandler = new ClientCommandHandler(writer, inStream, exchanger);

        Button btn_reg = new Button("register");
        btn_reg.setOnAction(event -> {
            GUI.RegistrationWindow.ShowRegistaionWindow(stage);
            stage.hide();
        });

        Button btn_log = new Button("login");
        btn_log.setOnAction(event -> {
            GUI.LoginWindow.ShowLoginWindow(stage);
            stage.hide();
        });

        root = new FlowPane(btn_log, btn_reg);

        scene = new Scene(root);

        stage.setScene(scene);

//        boolean exit = false;
//        try {
//            while (!exit) {
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                }
//
//                exit = cmdHandler.executeCommand();
//
//            }
//            try {
//                socket.close();
//                System.exit(0);
//            } catch (IOException e) {
//            }
//        } catch (Exception e) {
//
//        }
    }

    public static void main(String args[]) {

        try {
            if (args.length != 0) port = Integer.parseInt(args[0]);
            if (args.length > 1 ) host = args[1];
        } catch (Exception e) {
            System.out.println("Неверно задан порт/хост");
            System.exit(-1);
        }

        Application.launch(args);


    }

}
