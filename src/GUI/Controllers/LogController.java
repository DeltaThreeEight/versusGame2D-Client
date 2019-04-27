package GUI.Controllers;

import GUI.Main;
import ServerCon.ClientCommandHandler;
import Server.Command;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.ResourceBundle;

public class LogController {
    @FXML
    private Label log_lbl;
    @FXML
    private Label pass_lbl;
    @FXML
    private TextField login;
    @FXML
    private PasswordField pass;
    @FXML
    private Button btn_cnl;
    @FXML
    private Button btn_log;

    @FXML
    public void login() {
        if (pass.getText().trim().equals("")) {
            Main.showAlert("Введите пароль");
            return;
        } else if (login.getText().trim().equals("")) {
            Main.showAlert("Введите логин");
            return;
        }
        ClientCommandHandler.dH.sendCMD(new Command("login", login.getText(), pass.getText()));
    }

    public void localize() {
        ResourceBundle rb = Main.getMain().getRb();
        String log_field = rb.getString("reg_login");
        login.setPromptText(log_field);
        String logl = rb.getString("lbl_login");
        log_lbl.setText(logl);
        String passl = rb.getString("lbl_pass");
        pass_lbl.setText(passl);
        String pass_field = rb.getString("reg_pass");
        pass.setPromptText(pass_field);
        String log_btn = rb.getString("log_btn");
        btn_log.setText(log_btn);
        String cnl_btn = rb.getString("cnl_btn");
        btn_cnl.setText(cnl_btn);
    }

    @FXML
    public void hide() {

    }
}
