package GUI.Controllers;

import GUI.LoginWindow;
import GUI.Main;
import GUI.RegistrationWindow;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

public class Controller {

    private RegistrationWindow regWindow;
    private LoginWindow logWindow;

    @FXML
    private Button btn_reg;

    @FXML
    public void showRegWindow() {
        regWindow = new RegistrationWindow();
        Stage primaryStage = Main.getMain().getPrimaryStage();
        primaryStage.setScene(regWindow.getScen());
        primaryStage.setOnCloseRequest(event1 -> {
            primaryStage.setScene(Main.getMain().getPrimaryScene());
            primaryStage.setOnCloseRequest(event2 -> System.exit(0));
            Platform.runLater(() -> primaryStage.show());
        });
    }

    @FXML
    Button btn_log;

    public void localize() {
        ResourceBundle rb = Main.getMain().getRb();
        String btn_l = rb.getString("log_btn");
        btn_log.setText(btn_l);
        String btn_r = rb.getString("reg_btn");
        btn_reg.setText(btn_r);
    }

    @FXML
    public void showLogWindow() {
        logWindow = new LoginWindow();
        Stage primaryStage = Main.getMain().getPrimaryStage();
        primaryStage.setScene(logWindow.getScen());
        primaryStage.setOnCloseRequest(event1 -> {
            primaryStage.setScene(Main.getMain().getPrimaryScene());
            primaryStage.setOnCloseRequest(event2 -> System.exit(0));
            Platform.runLater(() -> primaryStage.show());
        });
    }
}
