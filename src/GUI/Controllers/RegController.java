package GUI.Controllers;

import GUI.Main;
import ServerCon.ClientCommandHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class RegController {
    @FXML
    private Label lbl_login;
    @FXML
    private Label lbl_email;
    @FXML
    private Label lbl_pass;
    @FXML
    private Label lbl_pass2;
    @FXML
    private Button reg_btn;
    @FXML
    private TextField login;
    @FXML
    private TextField email;
    @FXML
    private PasswordField pass;
    @FXML
    private PasswordField pass2;
    @FXML
    private Button cnl_btn;

    @FXML
    public void register() {
        if (login.getText().trim().equals("")) {
            Main.showAlert("Не введён логин!");
            return;
        }
        if (email.getText().trim().equals("")) {
            Main.showAlert("Не введена почта");
            return;
        }
        if (!pass.getText().equals(pass2.getText())) {
            Main.showAlert("Пароли не совпадают!");
            return;
        } else if (pass.getText().length() < 6) {
            Main.showAlert("Минимальная длина пароля - 6 символов!");
            return;
        }
        String hash = ClientCommandHandler.getHash(pass.getText());
        ClientCommandHandler.dH.sendToServer(String.format("register %s %s %s", login.getText(), email.getText(), hash));
    }

    public void localize() {
        ResourceBundle rb = Main.getMain().getRb();
        String reg = rb.getString("reg_btn");
        reg_btn.setText(reg);
        String log = rb.getString("reg_login");
        login.setPromptText(log);
        String mail = rb.getString("reg_email");
        email.setPromptText(mail);
        String pas = rb.getString("reg_pass");
        pass.setPromptText(pas);
        String pas2 = rb.getString("reg_pass2");
        pass2.setPromptText(pas2);
        String cnl = rb.getString("cnl_btn");
        cnl_btn.setText(cnl);
        String loginl = rb.getString("lbl_login");
        lbl_login.setText(loginl);
        String emaill = rb.getString("lbl_email");
        lbl_email.setText(emaill);
        String passl = rb.getString("lbl_pass");
        lbl_pass.setText(passl);
        String pass2l = rb.getString("lbl_pass2");
        lbl_pass2.setText(pass2l);
    }

    @FXML
    public void hide() {

    }
}
