package GUI;

import ServerCon.ClientCommandHandler;
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
                    RegistrationWindow.showAlert("Не введено имя персонажа");
                    return;
                }
                RadioButton sel = (RadioButton) group.getSelectedToggle();
                if (sel != null && sel.equals(mercBtn)) {
                    ClientCommandHandler.dH.executeCommand("createnew "+name.getText().replaceAll("\\s+","")+ " Merc");
                } else if (sel != null)
                    ClientCommandHandler.dH.executeCommand("createnew "+name.getText().replaceAll("\\s+","")+ " Spy");
                if (sel != null) instance.hide();
                    else RegistrationWindow.showAlert("Не выбрана сторона");
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
