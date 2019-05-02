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

    {
        setHealth(150);
    }

    public Merc(String name) {
        super(name);
        if (Math.random() > 0.5)
            getLocation().setXY(40,30);
        else
            getLocation().setXY(170,30);
    }

    public void show() {
        super.show("Merc.fxml");
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
