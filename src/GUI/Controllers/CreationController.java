package GUI.Controllers;

import Entities.Merc;
import Entities.Spy;
import GUI.Main;
import Server.Command;
import ServerCon.ClientCommandHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ResourceBundle;

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

    @FXML
    public void createPerson() {
        if (name.getText().trim().equals("")) {
            Main.showAlert("Не введено имя персонажа");
            return;
        }
        RadioButton sel = (RadioButton) group.getSelectedToggle();
        if (sel != null && sel.equals(merc)) {
            ClientCommandHandler.dH.executeCommand(new Command("createnew", name.getText().trim()));
            ClientCommandHandler.dH.sendHuman(new Merc(name.getText().trim()));
        } else if (sel != null) {
            ClientCommandHandler.dH.executeCommand(new Command("createnew", name.getText().trim()));
            ClientCommandHandler.dH.sendHuman(new Spy(name.getText().trim()));
        }
        if (sel == null)
            Main.showAlert("Не выбрана сторона");
    }

    public void localize() {
        group = new ToggleGroup();
        merc.setToggleGroup(group);
        spy.setToggleGroup(group);
        ResourceBundle rb = Main.getMain().getRb();
        String nm = rb.getString("prsn_name");
        name.setPromptText(nm);
        String sp = rb.getString("spy");
        spy.setText(sp);
        String mrc = rb.getString("merc");
        merc.setText(mrc);
    }
}
