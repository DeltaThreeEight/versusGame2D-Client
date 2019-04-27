package GUI.Controllers;

import GUI.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class LangController {

    @FXML
    private Button ok_btn;
    @FXML
    private ComboBox<String> langs;

    @FXML
    public void selectLang() {
        String lang = langs.getValue();
        Main.getMain().setLocale(lang);
        Main.getMain().tryConnect();
    }

    public void init() {
        ObservableList<String> lngs = FXCollections.observableArrayList("English", "Russian");
        langs.setItems(lngs);
        langs.setValue("Russian");
    }
}
