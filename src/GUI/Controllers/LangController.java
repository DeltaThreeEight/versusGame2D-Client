package GUI.Controllers;

import GUI.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class LangController {

    @FXML
    private Button ok_btn;
    @FXML
    private ComboBox<String> langs;
    private Main main;

    @FXML
    public void selectLang() {
        String lang = langs.getValue();
        main.setLocale(lang);
        main.tryConnect();
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void init() {
        ObservableList<String> lngs = FXCollections.observableArrayList("English", "Russian", "Japanese");
        langs.setItems(lngs);
        langs.setValue("Russian");
    }
}
