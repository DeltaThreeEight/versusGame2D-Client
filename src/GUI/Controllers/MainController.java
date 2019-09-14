package GUI.Controllers;

import Entities.Human;
import GUI.CreationWindow;
import GUI.Main;
import Server.Commands.ClientCommand;
import Network.Connection.ClientCommandHandler;
import Resources.TextResources;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

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
    private ListView<String> personsList;
    @FXML
    private Pane graphics;
    private Stage creationWindow;
    private ClientCommandHandler handler;

    public void setSelectedPerson(String text) {
        usr_prsn.setText(text);
    }

    public Pane getGraphics() {
        return graphics;
    }

    @FXML
    public void send() {
        if (msg.getText().trim().length() != 0) {
            handler.executeCMD(new ClientCommand("chat", msg.getText()));
        }
        msg.clear();
    }

    @FXML
    public void showAll() {
        handler.executeCMD(new ClientCommand("show_all"));
    }

    public void showAllPersons(String[] persons) {
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
        creationWindow.setResizable(false);
        creationWindow.setOnCloseRequest(event -> creationWindow = null);
        creationWindow.setScene(new CreationWindow(handler).getScreen());

        creationWindow.show();
    }

    public void setUserPersons(String[] persons) {
        personsList.setItems(FXCollections.observableArrayList(persons));
    }

    public void addPlayer(Human human) {
        graphics.getChildren().addAll(human);
    }

    public void addToChat(String add) {
        chat.setText(chat.getText()+add);
    }

    public void localize() {
        Main main = handler.getMain();

        main.getPrimaryStage().setMinHeight(460);
        main.getPrimaryStage().setMinWidth(800);

        TextResources resources = main.getTextResources();

        usr_prsn.setText(resources.DEFAULT_PERSON);

        btn_sel.setText(resources.BTN_SEL);
        btn_create.setText(resources.BTN_CRT);
        btn_stat.setText(resources.BTN_STAT);
        btn_del.setText(resources.BTN_DEL);
        all_prsns.setText(resources.BTN_ALL);
        btn_send.setText(resources.BTN_SEND);

        graphics.heightProperty().addListener((observable, oldValue, newValue) -> graphics.setClip(new Rectangle(graphics.getWidth(), graphics.getHeight())));
        graphics.widthProperty().addListener((observable, oldValue, newValue) -> graphics.setClip(new Rectangle(graphics.getWidth(), graphics.getHeight())));

    }

    @FXML
    public void select() {
        personCommand("select");
    }

    @FXML
    public void delete() {
        personCommand("remove");
    }

    @FXML
    public void stats() {
        personCommand("showstats");
    }

    private void personCommand(String command) {
        MultipleSelectionModel<String> selectedItems = personsList.getSelectionModel();
        ObservableList<String> persons = selectedItems.getSelectedItems();
        if (persons.size() == 1) {
            String name = persons.get(0).replaceAll("\n", "");
            handler.executeCMD(new ClientCommand(command, name));
        }
    }

    public void setHandler(ClientCommandHandler handler) {
        this.handler = handler;
    }
}
