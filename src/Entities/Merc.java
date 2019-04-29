package Entities;

import GUI.Controllers.HumanController;
import World.Location;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.LocalDateTime;

public class Merc extends Human {

    transient private int ammo = 60;

    {
        setHealth(150);
    }

    public Merc(String name) {
        super(name);
        getLocation().setXY(90,90);
    }

    public void show() {
        Pane root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Merc.fxml"));
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

    public Merc(String name, Location location) {
        super(name, location);
    }

    public Merc(String name, Location location, LocalDateTime date) {
        super(name, location, date);
    }

    public void shoot() {
        //TODO как же будет стрелять винтовка?
    }

}
