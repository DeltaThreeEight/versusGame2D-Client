package GUI.Controllers;

import GUI.Main;
import ServerCon.ClientCommandHandler;
import Server.Command;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ResourceBundle;

public class TokenController {
    @FXML
    private Button token_btn;
    @FXML
    private Label token_lbl;
    @FXML
    private TextField token_field;

    @FXML
    public void continReg() {
        ClientCommandHandler.dH.sendCMD(new Command("name", token_field.getText()));
        ClientCommandHandler.dH.getToken_window().close();
    }

    public void localize() {
        ResourceBundle rb = Main.getMain().getRb();
        String tok_field = rb.getString("reg_login");
        token_field.setPromptText(tok_field);
    }
}
