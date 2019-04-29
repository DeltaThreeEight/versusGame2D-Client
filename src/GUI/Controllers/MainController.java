package GUI.Controllers;

import Entities.Human;
import GUI.CreationWindow;
import GUI.Main;
import Server.Command;
import ServerCon.ClientCommandHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.ResourceBundle;

public class MainController {
    @FXML
    private Label chat;
    @FXML
    private Label usr_prsn;
    @FXML
    private TextArea msg;
    @FXML
    private Button btn_send;
    @FXML
    private Button btn_sel;
    @FXML
    private Button btn_del;
    @FXML
    private Button btn_create;
    @FXML
    private Button btn_stat;
    @FXML
    private Button all_prsns;
    @FXML
    private ListView<String> persons;
    @FXML
    private Pane graphics;
    private Stage creationWindow;

    @FXML
    public void select() {
        MultipleSelectionModel<String> selectedItems = persons.getSelectionModel();
        ObservableList<String> persons = selectedItems.getSelectedItems();
        if (persons.size() == 1) {
            String name = persons.get(0).replaceAll("\n", "");
            ClientCommandHandler.dH.executeCommand(new Command("select", name));
        }
    }

    public void setSelectedPerson(String text) {
        usr_prsn.setText(text);
    }

    public Pane getGraphics() {
        return graphics;
    }

    @FXML
    public void send() {
        if (!msg.getText().trim().equals("")) {
            ClientCommandHandler.dH.executeCommand(new Command("chat", msg.getText()));
        }
        msg.clear();
    }

    @FXML
    public void show_all() {
        ClientCommandHandler.dH.executeCommand(new Command("show_all"));
    }

    public void showAllPersons(ArrayList<String> persons) {
        Scene sc = new Scene(new FlowPane(new ScrollPane(new ListView<>(FXCollections.observableArrayList(persons)))));
        Stage allPlrs = new Stage();
        allPlrs.setScene(sc);
        allPlrs.showAndWait();
    }

    @FXML
    public void create() {
        if (creationWindow != null)
            return;
        creationWindow = new Stage();
        creationWindow.setAlwaysOnTop(true);
        creationWindow.setOnCloseRequest(event -> creationWindow = null);
        creationWindow.setResizable(false);
        creationWindow.setScene(new CreationWindow().getScen());
        creationWindow.show();
    }

    public Stage getCreationWindow() {
        Stage temp = creationWindow;
        creationWindow = null;
        return temp;
    }

    public void setUserPersons(ArrayList<String> persons) {
        this.persons.setItems(FXCollections.observableArrayList(persons));
    }

    public void addPlayer(Human human) {
        graphics.getChildren().addAll(human);
    }

    public void addToChat(String add) {
        chat.setText(chat.getText()+add);
    }

    public void localize() {
        Main.getMain().getPrimaryStage().setMinHeight(460);
        Main.getMain().getPrimaryStage().setMinWidth(800);
        ResourceBundle rb = Main.getMain().getRb();
        String usrPsrn = rb.getString("def_prsn");
        usr_prsn.setText(usrPsrn);
        String sel = rb.getString("btn_sel");
        btn_sel.setText(sel);
        String crt = rb.getString("btn_crt");
        btn_create.setText(crt);
        String stat = rb.getString("btn_stat");
        btn_stat.setText(stat);
        String del = rb.getString("btn_del");
        btn_del.setText(del);
        String all = rb.getString("btn_all");
        all_prsns.setText(all);
        String send = rb.getString("btn_send");
        btn_send.setText(send);
        graphics.heightProperty().addListener((observable, oldValue, newValue) -> graphics.setClip(new Rectangle(graphics.getWidth(), graphics.getHeight())));
        graphics.widthProperty().addListener((observable, oldValue, newValue) -> graphics.setClip(new Rectangle(graphics.getWidth(), graphics.getHeight())));
    }

    @FXML
    public void delete() {
        MultipleSelectionModel<String> selectedItems = persons.getSelectionModel();
        ObservableList<String> persons = selectedItems.getSelectedItems();
        if (persons.size() == 1) {
            String name = persons.get(0).replaceAll("\n", "");
            ClientCommandHandler.dH.executeCommand(new Command("remove", name));
        }
    }

    @FXML
    public void stats() {
        MultipleSelectionModel<String> selectedItems = persons.getSelectionModel();
        ObservableList<String> persons = selectedItems.getSelectedItems();
        if (persons.size() == 1) {
            String name = persons.get(0).replaceAll("\n", "");
            ClientCommandHandler.dH.executeCommand(new Command("showstats", name));
        }
    }
}
