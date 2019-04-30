package Entities;

import GUI.Controllers.HumanController;
import Server.Command;
import ServerCon.ClientCommandHandler;
import World.Location;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
        super.shoot();
    }

}
