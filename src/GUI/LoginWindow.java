package GUI;

import ServerCon.ClientCommandHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class LoginWindow {
    private static Stage instance = null;
    public static Stage primary = null;
    public static void ShowLoginWindow(Stage main) {
        if (instance == null) {
            primary = main;
            Label label = new Label("Login window");
            TextField login = new TextField("your login");
            PasswordField pass = new PasswordField();
            Button log = new Button("Login!");
            log.setOnAction(event -> {
                if (pass.getText().trim().equals("")) {
                    RegistrationWindow.showAlert("Введите пароль");
                    return;
                } else if (login.getText().trim().equals("")) {
                    RegistrationWindow.showAlert("Введите логин");
                    return;
                }
                String hash = ClientCommandHandler.getHash(pass.getText());
                ClientCommandHandler.dH.sendToServer(String.format("login %s %s", login.getText(), hash));
            });
            FlowPane flow = new FlowPane(Orientation.VERTICAL,label,login, pass, log);
            instance = new Stage();
            instance.setScene(new Scene(flow));
            instance.setOnCloseRequest(event -> main.show());
            instance.show();
        } else {
            instance.show();
        }
    }

    static void close() {
        if (instance != null) {
            instance.close();
            instance = null;
        }
    }
}
