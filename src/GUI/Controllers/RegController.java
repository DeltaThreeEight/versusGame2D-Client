package GUI.Controllers;

import GUI.Main;
import Server.Commands.ClientCommand;
import Network.Connection.ClientCommandHandler;
import Resources.TextResources;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


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
    private ClientCommandHandler handler;
    private Main main;

    @FXML
    public void register() {
        handler.executeCMD(new ClientCommand("register", login.getText(), email.getText(), pass.getText(), pass2.getText()));
    }

    public void setHandler(ClientCommandHandler handler) {
        this.handler = handler;
        this.main = handler.getMain();
    }

    public void localize() {
        TextResources resources = main.getTextResources();

        reg_btn.setText(resources.REG_BTN);
        cnl_btn.setText(resources.CNL_BTN);

        login.setPromptText(resources.REG_LOGIN);
        email.setPromptText(resources.REG_EMAIL);
        pass.setPromptText(resources.REG_PASS);
        pass2.setPromptText(resources.REG_PASS2);

        lbl_login.setText(resources.LBL_LOGIN);
        lbl_email.setText(resources.LBL_EMAIL);
        lbl_pass.setText(resources.LBL_PASS);
        lbl_pass2.setText(resources.LBL_PASS2);
    }

    @FXML
    public void hide() {
        main.getPrimaryStage().setScene(main.getPrimaryScene());
        main.getPrimaryStage().setOnCloseRequest(event2 -> System.exit(0));
    }
}
