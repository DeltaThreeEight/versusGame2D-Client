package GUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

public class HumanController {
    @FXML
    private Rectangle col_rec;
    @FXML
    private Ellipse right_hand;
    @FXML
    private Ellipse left_hand;
    @FXML
    private Ellipse head;
    @FXML
    private Ellipse body;

    public Rectangle getCol_rec() {
        return col_rec;
    }

    public Ellipse getBody() {
        return body;
    }

    public Ellipse getHead() {
        return head;
    }

    public Ellipse getLeft_hand() {
        return left_hand;
    }

    public Ellipse getRight_hand() {
        return right_hand;
    }
}
