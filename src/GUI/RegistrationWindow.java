package GUI;

import ServerCon.ClientCommandHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RegistrationWindow {
    private static Stage instance = null;
    public static void ShowRegistaionWindow(Stage main) {
        if (instance == null) {

            Label label = new Label("registration window");
            TextField login = new TextField("your login");
            TextField email = new TextField("you email");
            PasswordField pass = new PasswordField();
            PasswordField pass2 = new PasswordField();
            Button reg = new Button("Register!");
            reg.setOnAction(event -> {
                if (!pass.getText().equals(pass2.getText())) {
                    showAlert("Пароли не совпадают!");
                    return;
                } else if (pass.getText().length() < 6) {
                    showAlert("Минимальная длина пароля - 6 символов!");
                    return;
                }
                String hash = ClientCommandHandler.getHash(pass.getText());
                ClientCommandHandler.dH.sendToServer(String.format("register %s %s %s", login.getText(), email.getText(), hash));
            });
            FlowPane flow = new FlowPane(Orientation.VERTICAL,label,login, email, pass, pass2, reg);
            instance = new Stage();
            instance.setScene(new Scene(flow));
            instance.setOnCloseRequest(event -> main.show());
            instance.show();
        } else {
            instance.show();
        }
    }

    public static void showAlert(String text) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Регистрация/Авторизация");
        dialog.setHeaderText(text);
        if (text.trim().equals("Авторизация успешна")) {
            ClientCommandHandler.mainWindow = new MainWindow();
            ClientCommandHandler.mainWindow.showMain();
        }
        dialog.show();
    }

    static void close() {
        if (instance != null)
        instance.close();
    }
}
