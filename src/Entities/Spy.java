package Entities;

import GUI.Controllers.HumanController;
import World.Location;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.LocalDateTime;

public class Spy extends Human {

    {
        setSpeedModifier(1.5);
    }

    public Spy(String name) {
        super(name);
        getLocation().setXY(10,10);
    }

    public void show() {
        Pane root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Spy.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        setCol_rec(((HumanController) loader.getController()).getCol_rec());
        setTranslateY(getLocation().getY());
        setTranslateX(getLocation().getX());

        Label nm = new Label(getName());
        setOrientation(Orientation.VERTICAL);
        getChildren().addAll(nm, root);
    }

    public Spy(String name, Location location) {
        super(name, location);
    }

    public Spy(String name, Location location, LocalDateTime date) {
        super(name, location, date);
    }

    public void shoot() {
        //TODO придумать как же стерлять из шокера
    }
}
