package Entities;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class BigWall extends Pane {
    private Rectangle wall;

    public BigWall(Rectangle w) {
        wall = w;
    }

    public Rectangle getWall() {
        return wall;
    }
}
