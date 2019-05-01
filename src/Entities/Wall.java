package Entities;

import GUI.Controllers.HumanController;
import GUI.Controllers.WallController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class Wall extends Pane {
    private Rectangle wall;
    private double x;
    private double y;

    public Wall(double _x, double _y) {
        x = _x;
        y = _y;
    }

    public void show() {
        Pane root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Wall.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        wall = ((WallController) loader.getController()).getWall();

        setTranslateY(y);
        setTranslateX(x);

        getChildren().addAll(root);
    }

    public Rectangle getWall() {
        return wall;
    }
}
