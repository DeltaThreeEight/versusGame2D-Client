package GUI;

import Entities.Merc;
import Entities.Spy;
import ServerCon.ClientCommandHandler;
import Server.Command;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class PrsnCrtnWindow {
    private static Stage instance = null;
    public static void ShowCreationWindow() {
        if (instance == null) {
            TextField name = new TextField("Имя");

            RadioButton spyBtn = new RadioButton("Шпион");
            RadioButton mercBtn = new RadioButton("Наёмник");

            ToggleGroup group = new ToggleGroup();
            spyBtn.setToggleGroup(group);
            mercBtn.setToggleGroup(group);
            Button create = new Button("Создать");
            create.setOnAction(event -> {
                if (name.getText().trim().equals("")) {
                    Main.showAlert("Не введено имя персонажа");
                    return;
                }
                RadioButton sel = (RadioButton) group.getSelectedToggle();
                if (sel != null && sel.equals(mercBtn)) {
                    ClientCommandHandler.dH.executeCommand(new Command("createnew", name.getText().trim()));
                    ClientCommandHandler.dH.sendHuman(new Merc(name.getText().trim()));
                } else if (sel != null) {
                    ClientCommandHandler.dH.executeCommand(new Command("createnew", name.getText().trim()));
                    ClientCommandHandler.dH.sendHuman(new Spy(name.getText().trim()));
                }
                if (sel != null) instance.hide();
                    else Main.showAlert("Не выбрана сторона");
            });
            FlowPane flow = new FlowPane(Orientation.VERTICAL, name, spyBtn, mercBtn, create);
            instance = new Stage();
            instance.setScene(new Scene(flow));
            instance.show();
        } else {
            instance.show();
        }
    }

    static void close() {
        if (instance != null)
            instance.close();
    }
}
