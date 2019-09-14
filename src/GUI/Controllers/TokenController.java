package GUI.Controllers;

import GUI.Main;
import Server.Commands.ClientCommand;
import Network.Connection.ClientCommandHandler;
import Resources.TextResources;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class TokenController {
    @FXML
    private Button token_btn;
    @FXML
    private Label token_lbl;
    @FXML
    private TextField token_field;
    private ClientCommandHandler handler;
    private Main main;

    @FXML
    public void sendRegistrationToken() {
        handler.sendCMD(new ClientCommand("name", token_field.getText()));
        handler.getTokenWindow().close();
    }

    public void localize() {
        TextResources resources = main.getTextResources();

        token_lbl.setText(resources.INPUT_TOKEN);
        token_btn.setText(resources.ENTER);
    }

    public void setHandler(ClientCommandHandler handler) {
        this.handler = handler;
        this.main = handler.getMain();
    }
}
