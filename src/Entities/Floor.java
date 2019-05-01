package Entities;

import GUI.Controllers.WallController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class Floor extends Pane {
    private double x;
    private double y;

    public Floor(double _x, double _y) {
        x = _x;
        y = _y;
    }

    public void show() {
        Pane root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Floor.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        setTranslateY(y);
        setTranslateX(x);

        getChildren().addAll(root);
    }

}
