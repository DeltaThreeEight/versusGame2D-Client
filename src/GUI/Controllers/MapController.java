package GUI.Controllers;

import Entities.BigWall;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class MapController {
    @FXML
    private Rectangle wallA;
    @FXML
    private Rectangle wallB;
    @FXML
    private Rectangle wallC;
    @FXML
    private Rectangle wallD;
    @FXML
    private Rectangle wallE;
    @FXML
    private Rectangle wallF;
    @FXML
    private Rectangle wallG;
    @FXML
    private Rectangle wallH;
    @FXML
    private Rectangle wallI;
    @FXML
    private Rectangle wallK;
    @FXML
    private Rectangle wallM;
    @FXML
    private Rectangle wallN;

    public ArrayList<BigWall> getAllWalls() {
        ArrayList<BigWall> a = new ArrayList<>();
        a.add(new BigWall(wallA));
        a.add(new BigWall(wallB));
        a.add(new BigWall(wallC));
        a.add(new BigWall(wallD));
        a.add(new BigWall(wallE));
        a.add(new BigWall(wallF));
        a.add(new BigWall(wallG));
        a.add(new BigWall(wallH));
        a.add(new BigWall(wallI));
        a.add(new BigWall(wallK));
        a.add(new BigWall(wallM));
        a.add(new BigWall(wallN));
        return a;
    }
}
