package Entities;

import ServerCon.ClientCommandHandler;
import World.Location;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../GUI/fxml/Merc.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
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
