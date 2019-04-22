package GUI;

import Entities.Human;
import ServerCon.ClientCommandHandler;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;


public class MainWindow extends Stage {
    private Label text = new Label("");
    private ListView<String> activeView = new ListView<>();
    private ScrollPane persons = new ScrollPane(activeView);
    private HashMap<KeyCode, Boolean> keys = new HashMap<>();
    private Label selectedPerson = new Label("Вы не выбрали персонажа");
    private Stage allPlrs = new Stage();
    private Pane graphics = new Pane();

    private Image image1 = new Image(getClass().getResourceAsStream("map.png"));
    private ImageView imageView1 = new ImageView(image1);

    public MainWindow() {
        TextArea textArea = new TextArea();
        textArea.setPrefHeight(50);
        textArea.setPrefWidth(100);

        Button kndUsr = new Button("Все персонажи");
        kndUsr.setOnAction( event -> ClientCommandHandler.dH.executeCommand("show_all"));

        Button select = new Button("select");
        select.setOnAction(event -> {
            MultipleSelectionModel<String> selectedItems = activeView.getSelectionModel();
            ObservableList<String> persons = selectedItems.getSelectedItems();
            if (persons.size() == 1) {
                String name = persons.get(0).replaceAll("\n", "");
                ClientCommandHandler.dH.executeCommand("select "+name);
            }
        });

        Button stats = new Button("Stats");
        stats.setOnAction(event -> {
            MultipleSelectionModel<String> selectedItems = activeView.getSelectionModel();
            ObservableList<String> persons = selectedItems.getSelectedItems();
            if (persons.size() == 1) {
                String name = persons.get(0).replaceAll("\n", "");
                ClientCommandHandler.dH.executeCommand("showstats "+name);
            }
        });

        Button test = new Button("test");
        test.setOnAction(event -> close());

        Button btn = new Button("Отправить");
        btn.setPrefHeight(40);
        btn.setPrefWidth(100);

        ClientCommandHandler.dH.executeCommand("show");

        ScrollPane chat = new ScrollPane(text);
        chat.setPrefViewportHeight(300);
        chat.setPrefViewportWidth(100);


        btn.setOnAction(event -> {
            if (!textArea.getText().trim().equals("")) {
                ClientCommandHandler.dH.executeCommand("chat " + textArea.getText());
            }
            textArea.clear();
        });

        Button crtPrsn = new Button("Создать персонажа");
        crtPrsn.setOnAction(event -> PrsnCrtnWindow.ShowCreationWindow());

        Button rmvPrsn = new Button("Удалить персонажа");
        rmvPrsn.setOnAction(event -> {
            MultipleSelectionModel<String> selectedItems = activeView.getSelectionModel();
            ObservableList<String> persons = selectedItems.getSelectedItems();
            if (persons.size() == 1) {
                String name = persons.get(0).replaceAll("\n", "");
                ClientCommandHandler.dH.executeCommand("remove " + name);
            } });

        graphics.getChildren().addAll(imageView1);
        graphics.setPrefSize(600,300);
        Scene mainScene = new Scene(new FlowPane(chat, textArea, btn,
                persons, kndUsr, selectedPerson, select, stats, rmvPrsn, crtPrsn,
                graphics));

        this.setScene(mainScene);
        this.setOnCloseRequest(event ->
                System.exit(0)
        );

        this.setWidth(1000);
        this.setHeight(800);
        this.setTitle("Main Window");

        //mainScene.setOnKeyPressed(event-> keys.put(event.getCode(),true));
        //mainScene.setOnKeyReleased(event-> keys.put(event.getCode(), false));

        mainScene.setOnKeyPressed(ke -> {
            KeyCode keyCode = ke.getCode();
            switch (keyCode.toString()) {
                case "W":
                    ClientCommandHandler.dH.executeCommand("move FORWARD");
                    break;
                case "S":
                    ClientCommandHandler.dH.executeCommand("move BACK");
                    break;
                case "A":
                    ClientCommandHandler.dH.executeCommand("move LEFT");
                    break;
                case "D":
                    ClientCommandHandler.dH.executeCommand("move RIGHT");
                    break;
            }
        });
    }

    public void showMain() {
        this.show();
        LoginWindow.close();
        RegistrationWindow.close();
    }


    public void addPlayer(Human human) {
        graphics.getChildren().addAll(human);
    }

    public void addToChat(String add) {
        text.setText(text.getText()+add);
    }

    public void setUserPersons(ArrayList<String> persons) {
        activeView.setItems(FXCollections.observableArrayList(persons));
    }

    public void showAllPersons(ArrayList<String> persons) {
        Scene sc = new Scene(new FlowPane(new ScrollPane(new ListView<>(FXCollections.observableArrayList(persons)))));
        allPlrs.setScene(sc);
        allPlrs.showAndWait();
    }

    public void setSelectedPerson(String name) {
        selectedPerson.setText("Ваш персонаж: "+name);
    }

    public void closeMain() {
        LoginWindow.ShowLoginWindow(LoginWindow.primary);
        this.close();
    }
}
