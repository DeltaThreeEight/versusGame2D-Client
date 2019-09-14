package GUI.Controllers;

import Entities.Merc;
import Entities.Spy;
import Server.Commands.ClientCommand;
import Network.Connection.ClientCommandHandler;
import Resources.TextResources;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CreationController {
    @FXML
    private Button ok_btn;
    @FXML
    private RadioButton spy;
    @FXML
    private RadioButton merc;
    @FXML
    private TextField name;
    private ToggleGroup group;
    private ClientCommandHandler handler;

    @FXML
    public void createPerson() {
        if (name.getText().trim().equals("")) {
            handler.getMain().showAlert(handler.getMain().getTextResources().ENTER_PRSN_NAME);
            return;
        }

        RadioButton sel = (RadioButton) group.getSelectedToggle();

        if (sel.equals(spy))
            handler.executeCMD(new ClientCommand("createnew", new Spy(name.getText().trim())));
        else
            handler.executeCMD(new ClientCommand("createnew", new Merc(name.getText().trim())));
    }

    public void setHandler(ClientCommandHandler handler) {
        this.handler = handler;
    }

    public void localize() {
        group = new ToggleGroup();

        merc.setToggleGroup(group);
        spy.setToggleGroup(group);
        group.selectToggle(spy);

        TextResources resources = handler.getMain().getTextResources();

        name.setPromptText(resources.PRSN_NAME);

        spy.setText(resources.SPY);
        merc.setText(resources.MERC);
    }
}
