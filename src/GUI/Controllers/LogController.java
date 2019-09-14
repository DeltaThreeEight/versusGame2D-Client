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
    private ClientCommandHandler handler;
    private Main main;

    @FXML
    public void login() {
        handler.executeCMD(new ClientCommand("login", login.getText(), pass.getText()));
    }

    public void setHandler(ClientCommandHandler handler) {
        this.handler = handler;
        this.main = handler.getMain();
    }

    public void localize() {
        TextResources resources = main.getTextResources();

        log_lbl.setText(resources.LBL_LOGIN);
        pass_lbl.setText(resources.LBL_PASS);

        login.setPromptText(resources.REG_LOGIN);
        pass.setPromptText(resources.REG_PASS);

        btn_log.setText(resources.LOG_BTN);
        btn_cnl.setText(resources.CNL_BTN);
    }

    @FXML
    public void hide() {
        main.getPrimaryStage().setScene(main.getPrimaryScene());
        main.getPrimaryStage().setOnCloseRequest(event -> System.exit(0));
    }
}
