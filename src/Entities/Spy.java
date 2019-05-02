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
        if (Math.random() > 0.5)
            getLocation().setXY(270,350);
        else
            getLocation().setXY(20,350);
    }

    public void show() {
        super.show("Spy.fxml");
    }

    public Spy(String name, Location location) {
        super(name, location);
    }

    public Spy(String name, Location location, LocalDateTime date) {
        super(name, location, date);
    }

    public void shoot() {
        super.shoot();
    }
}
