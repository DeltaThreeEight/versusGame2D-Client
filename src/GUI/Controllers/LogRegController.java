package GUI.Controllers;

import GUI.LoginWindow;
import GUI.Main;
import GUI.RegistrationWindow;
import Network.Connection.ClientCommandHandler;
import Resources.TextResources;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class LogRegController {

    private ClientCommandHandler handler;
    private Main main;

    @FXML
    private Button btn_reg;

    @FXML
    Button btn_log;

    public void setHandler(ClientCommandHandler handler) {
        this.handler = handler;
        this.main = handler.getMain();
    }

    public void localize() {
        TextResources resources = main.getTextResources();

        btn_log.setText(resources.LOG_BTN);
        btn_reg.setText(resources.REG_BTN);
    }

    private void showWindow(Scene scene) {
        Stage primaryStage = main.getPrimaryStage();

        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            primaryStage.setScene(main.getPrimaryScene());
            primaryStage.setOnCloseRequest(eventInner -> System.exit(0));
            Platform.runLater(primaryStage::show);
        });
    }

    @FXML
    public void showLogWindow() {
        LoginWindow logWindow = new LoginWindow(handler);
        showWindow(logWindow.getScreen());
    }

    @FXML
    public void showRegWindow() {
        RegistrationWindow regWindow = new RegistrationWindow(handler);
        showWindow(regWindow.getScreen());
    }
}
