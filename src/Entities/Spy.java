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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../GUI/fxml/Spy.fxml"));
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
