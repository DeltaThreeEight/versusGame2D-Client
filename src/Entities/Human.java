package Entities;

import Entities.exceptions.NotAliveException;
import GUI.Controllers.HumanController;
import GUI.MainWindow;
import Server.Commands.ClientCommand;
import Network.Connection.ClientCommandHandler;
import World.Location;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Human extends FlowPane implements Moveable, Comparable<Human>, Serializable {
    private String name;
    private Location loc;
    private int hp = 100;
    private int ammo = 30;
    private Moves lastMove = Moves.BACK;
    private LocalDateTime dateOfCreation;
    private double speedModifier = 1.0;
    private String user = "default";
    private Rectangle col_rec;
    protected Ellipse body;
    protected Ellipse head;
    protected Ellipse right_hand;
    protected Ellipse left_hand;
    protected Rectangle right_arm;
    protected Rectangle left_arm;
    protected Rectangle gun;
    protected Pane root;

    private transient ClientCommandHandler handler;

    public Moves getLastMove() {
        return lastMove;
    }

    public Rectangle getCollisionBox() {
        return col_rec;
    }

    public void setCollisionBox(Rectangle col_rec) {
        this.col_rec = col_rec;
    }

    public void setUser(String str) {
        user = str;
    }

    public String getUser() {
        return user;
    }

    public Human(String iName) {
        this.name = iName;
        this.loc = new Location(0, 0);
        dateOfCreation = LocalDateTime.now();
    }

    public Human(String iName, Location iLoc) {
        this.name = iName;
        this.loc = iLoc;
        dateOfCreation = LocalDateTime.now();
    }

    public Human(String iName, Location iLoc, LocalDateTime date) {
        this.name = iName;
        this.loc = iLoc;
        dateOfCreation = date;
        setTranslateY(loc.getY());
        setTranslateX(loc.getX());
    }

    public void setHandler(Object handler) {
        this.handler = (ClientCommandHandler) handler;
    }

    public void show() {
        show("Spy.fxml");
    }

    public void show(String res) {
        root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(res));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        setTranslateY(getLocation().getY());
        setTranslateX(getLocation().getX());

        HumanController humanController = loader.getController();
        body = humanController.getBody();
        left_hand = humanController.getLeftHand();
        right_hand = humanController.getRightHand();
        head = humanController.getHead();


        Label nm = new Label(getName());
        nm.setTextFill(Paint.valueOf("WHITE"));
        setOrientation(Orientation.VERTICAL);
        setCollisionBox(humanController.getCollisionBox());
        getChildren().addAll(nm, root);
        if (getLastMove() == Moves.FORWARD || getLastMove() == Moves.BACK)
            rotare(true);

        left_arm = new Rectangle();
        left_arm.setFill(Paint.valueOf("#303336"));
        left_arm.setStroke(Paint.valueOf("BLACK"));
        left_arm.setSmooth(true);
        left_arm.setArcWidth(5);
        left_arm.setArcHeight(5);
        left_arm.setWidth(5);
        left_arm.setHeight(17);
        left_arm.setRotate(-45);

        right_arm = new Rectangle();
        right_arm.setFill(Paint.valueOf("#303336"));
        right_arm.setStroke(Paint.valueOf("BLACK"));
        right_arm.setSmooth(true);
        right_arm.setArcWidth(5);
        right_arm.setArcHeight(5);
        right_arm.setWidth(5);
        right_arm.setHeight(17);
        right_arm.setRotate(45);

        gun = new Rectangle();
        gun.setFill(Paint.valueOf("#4d3232"));
        gun.setStroke(Paint.valueOf("BLACK"));
        gun.setSmooth(true);
        gun.setArcWidth(5);
        gun.setArcHeight(5);
        gun.setWidth(8);
        gun.setHeight(6);
    }

    public void hide() {
        try {
            handler.getMainWindow().getMainController().getGraphics().getChildren().removeAll(this);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        getChildren().clear();
    }

    public void moveOther(Moves move) {
        if (isAlive()) {

            if (root.getChildren().contains(gun)) {
                root.getChildren().removeAll(left_arm, right_arm, gun);
            }

            lastMove = move;
            setTranslateY(getTranslateY() + move.getY() * speedModifier);
            setTranslateX(getTranslateX() + move.getX() * speedModifier);

            boolean intersects = checkIntersects(move);

            if (!intersects) {
                loc.setXY(loc.getX() + move.getX() * speedModifier, loc.getY() + move.getY() * speedModifier);
            }
        }
    }

    public void teleportOther(double x, double y) {
        setTranslateX(x);
        setTranslateY(y);
        loc.setXY(x, y);
    }

    public void teleport(double x, double y) {
        setTranslateX(x);
        setTranslateY(y);
        loc.setXY(x, y);
        handler.executeCMD(new ClientCommand("teleport", x+"", y+""));
    }

    public boolean checkIntersects(Moves move) {

        for (Node n : handler.getMainWindow().getMainController().getGraphics().getChildren()) {

            if (n instanceof Human) {

                Human h = (Human) n;

                if (h != this) {
                    if (((Path) Shape.intersect(col_rec, h.getCollisionBox())).getElements().size() > 0) {

                        System.out.println("Касание");

                        setTranslateY(getTranslateY() - move.getY() * speedModifier);
                        setTranslateX(getTranslateX() - move.getX() * speedModifier);

                        while (((Path) Shape.intersect(col_rec, h.getCollisionBox())).getElements().size() > 0) {
                            teleport(loc.getX() + 5, loc.getY() + 5);
                        }

                        return true;
                    }
                }
            }
            if (n instanceof BigWall) {

                BigWall h = (BigWall) n;

                if (((Path) Shape.intersect(col_rec, h.getWall())).getElements().size() > 0) {

                    System.out.println("Касание");

                    setTranslateY(getTranslateY() - move.getY() * speedModifier);
                    setTranslateX(getTranslateX() - move.getX() * speedModifier);

                    while (((Path) Shape.intersect(col_rec, h.getWall())).getElements().size() > 0) {
                        teleport(loc.getX() + 5, loc.getY() + 5);
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public void setLastMove(Moves move) {
        lastMove = move;
    }

    public void rotare(boolean b) {
        if (root.getChildren().contains(gun)) {
            Platform.runLater(() -> root.getChildren().removeAll(left_arm, right_arm, gun));
        }
        if (b) {
            body.setRadiusX(10);
            body.setRadiusY(7);

            left_hand.setRadiusY(4);
            left_hand.setRadiusX(3);
            left_hand.setCenterX(left_hand.getCenterX()-10);
            left_hand.setCenterY(left_hand.getCenterY()+10);

            right_hand.setRadiusY(4);
            right_hand.setRadiusX(3);
            right_hand.setCenterX(right_hand.getCenterX()+10);
            right_hand.setCenterY(right_hand.getCenterY()-10);
        } else {
            body.setRadiusX(7);
            body.setRadiusY(10);

            left_hand.setRadiusY(3);
            left_hand.setRadiusX(4);
            left_hand.setCenterX(left_hand.getCenterX()+10);
            left_hand.setCenterY(left_hand.getCenterY()-10);

            right_hand.setRadiusY(3);
            right_hand.setRadiusX(4);
            right_hand.setCenterX(right_hand.getCenterX()-10);
            right_hand.setCenterY(right_hand.getCenterY()+10);
        }
    }

    public void move(Moves move) throws NotAliveException {
        if (isAlive()) {
            if (lastMove == Moves.RIGHT || lastMove == Moves.LEFT) {
                if (move == Moves.RIGHT || move == Moves.LEFT) {
                    // do nothing
                } else {
                    handler.executeCMD(new ClientCommand("rotare", move.toString()));
                    rotare(true);
                }
            } else {
                if (move == Moves.BACK || move == Moves.FORWARD) {
                    // do nothing
                } else {
                    handler.executeCMD(new ClientCommand("rotare", move.toString()));
                    rotare(false);
                }
            }
            if (root.getChildren().contains(gun)) {
                root.getChildren().removeAll(left_arm, right_arm, gun);
            }
            lastMove = move;

            setTranslateY(getTranslateY() + move.getY() * speedModifier);
            setTranslateX(getTranslateX() + move.getX() * speedModifier);

            boolean intersects = checkIntersects(move);

            if (!intersects) {
                loc.setXY(loc.getX() + move.getX() * speedModifier, loc.getY() + move.getY() * speedModifier);
                System.out.println("Перемещение " + loc);
                handler.executeCMD(new ClientCommand("move", move.toString()));
            }
        } else System.out.println("Перемещние невозможно");

    }

    public void shootOther() {
        if (ammo > 0) {
            shootAnim();

            System.out.println("SHOOT");
            final Shape bullet = new Circle(2, Color.ORANGE);
            handler.getMainWindow().getMainController().getGraphics().getChildren().add(bullet);
            final TranslateTransition bulletAnimation = new TranslateTransition(Duration.seconds(2), bullet);

            int modx = 0;
            int mody = 0;
            switch (lastMove) {
                case LEFT:
                    modx = -16;
                    break;
                case RIGHT:
                    modx = 16;
                    break;
                case FORWARD:
                    mody = 16;
                    break;
                case BACK:
                    mody = -16;
                    break;
            }

            ((Circle) bullet).setCenterX(getLocation().getX() + 16 + modx);
            ((Circle) bullet).setCenterY(getLocation().getY() + 32 + mody);

            bulletAnimation.setToX(getLastMove().getX() * 1000);
            bulletAnimation.setToY(getLastMove().getY() * 1000);

            bullet.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
                Pane fr = handler.getMainWindow().getMainController().getGraphics();
                for (Node n : fr.getChildren()) {
                    if (n instanceof Human) {
                        Human h = (Human) n;
                        if (h.getCollisionBox() != this.getCollisionBox()) {
                            if (((Path) Shape.intersect(bullet, h.getCollisionBox())).getElements().size() > 0) {
                                System.out.println("Hit!");
                                h.setHealth(h.getHealth() - 10);
                                if (h == handler.getPlayerClient()) {
                                    double maxHealt = 0;
                                    if (h instanceof Spy)
                                        maxHealt = 100;
                                    else maxHealt = 150;
                                    handler.getHpBar().setWidth(56.0 * (((double) h.getHealth()) / maxHealt));
                                }
                                bulletAnimation.stop();
                                fr.getChildren().remove(bullet);
                                break;
                            }
                        }
                    }
                    if (n instanceof BigWall) {
                        BigWall h = (BigWall) n;
                        if (((Path) Shape.intersect(bullet, h.getWall())).getElements().size() > 0) {
                            System.out.println("Hit!");
                            bulletAnimation.stop();
                            fr.getChildren().remove(bullet);
                            break;
                        }
                    }
                }

            });

            bulletAnimation.setOnFinished(event -> handler.getMainWindow().getMainController().getGraphics().getChildren().remove(bullet));
            bulletAnimation.play();
        } else {
            System.out.println("Reloading...");
        }
    }

    public int getAmmo() {
        return ammo;
    }

    public void reload() {
        ammo = 30;
        handler.getAmmoAmount().setText(ammo+"");
    }

    public void shootAnim() {

        switch (lastMove) {
            case RIGHT:
                left_arm.setLayoutX(20);
                left_arm.setLayoutY(2);
                right_arm.setLayoutX(20);
                right_arm.setLayoutY(13);
                gun.setLayoutX(24);
                gun.setLayoutY(13);
                gun.setWidth(8);
                gun.setHeight(6);
                break;
            case LEFT:
                left_arm.setLayoutX(6);
                left_arm.setLayoutY(12);
                right_arm.setLayoutX(6);
                right_arm.setLayoutY(3);
                gun.setLayoutX(0);
                gun.setLayoutY(13);
                gun.setWidth(8);
                gun.setHeight(6);
                break;
            case FORWARD:
                left_arm.setLayoutX(6);
                left_arm.setLayoutY(12);
                right_arm.setLayoutX(20);
                right_arm.setLayoutY(13);
                gun.setLayoutX(12);
                gun.setLayoutY(25);
                gun.setWidth(6);
                gun.setHeight(8);
                break;
            case BACK:
                left_arm.setLayoutX(20);
                left_arm.setLayoutY(2);
                right_arm.setLayoutX(6);
                right_arm.setLayoutY(3);
                gun.setLayoutX(12);
                gun.setLayoutY(0);
                gun.setWidth(6);
                gun.setHeight(8);
                break;
        }

        if (!root.getChildren().contains(gun)) {
            root.getChildren().addAll(left_arm);
            left_arm.toBack();

            root.getChildren().addAll(right_arm);
            right_arm.toBack();

            root.getChildren().addAll(gun);
        }
    }

    public void shoot() {
        if (ammo > 0) {
            ammo--;
            shootAnim();
            handler.getAmmoAmount().setText(ammo+"");
            handler.executeCMD(new ClientCommand("shoot"));
            System.out.println("SHOOT");
            final Shape bullet = new Circle(2, Color.ORANGE);

            MainWindow mainWindow = handler.getMainWindow();
            mainWindow.getMainController().getGraphics().getChildren().add(bullet);

            final TranslateTransition bulletAnimation = new TranslateTransition(Duration.seconds(2), bullet);

            int modx = 0;
            int mody = 0;
            switch (lastMove) {
                case LEFT:
                    modx = -16;
                    break;
                case RIGHT:
                    modx = 16;
                    break;
                case FORWARD:
                    mody = 16;
                    break;
                case BACK:
                    mody = -16;
                    break;
            }

            ((Circle) bullet).setCenterX(getLocation().getX() + 16 + modx);
            ((Circle) bullet).setCenterY(getLocation().getY() + 34 + mody);

            bulletAnimation.setToX(getLastMove().getX() * 1000);
            bulletAnimation.setToY(getLastMove().getY() * 1000);

            bullet.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
                Pane fr = mainWindow.getMainController().getGraphics();
                for (Node n : fr.getChildren()) {
                    if (n instanceof Human) {
                        Human h = (Human) n;
                        if (h.getCollisionBox() != this.getCollisionBox()) {
                            if (((Path) Shape.intersect(bullet, h.getCollisionBox())).getElements().size() > 0) {
                                System.out.println("Hit!");
                                h.setHealth(h.getHealth() - 10);
                                handler.executeCMD(new ClientCommand("hit", h.getName()));
                                bulletAnimation.stop();
                                fr.getChildren().remove(bullet);
                                break;
                            }
                        }
                    }
                    if (n instanceof BigWall) {
                        BigWall h = (BigWall) n;
                        if (((Path) Shape.intersect(bullet, h.getWall())).getElements().size() > 0) {
                            System.out.println("Hit!");
                            bulletAnimation.stop();
                            fr.getChildren().remove(bullet);
                            break;
                        }
                    }
                }

            });

            bulletAnimation.setOnFinished(event -> mainWindow.getMainController().getGraphics().getChildren().remove(bullet));
            bulletAnimation.play();

        } else {
            handler.executeCMD(new ClientCommand("shoot"));
            System.out.println("Reloading...");
        }

    }

    public int getHealth() {
        return hp;
    }

    public Location getLocation() {
        return loc;
    }

    public double getSpeedModifier() {
        return speedModifier;
    }

    public void setSpeedModifier(Double mod) {
        speedModifier = mod;
    }

    public void died(Human human) {
        hp = 0;
        System.out.println(name + " был убит " + human.getName() + "ом");
    }

    public String getName() {
        return name;
    }

    public boolean isAlive() {
        return hp > 0 ? true : false;
    }

    @Override
    public String toString() {
        return name + getClass().toString().replace("class Entities.", " ") + " "+ loc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Human)) return false;
        Human human = (Human) o;
        return hp == human.hp &&
                Double.compare(human.speedModifier, speedModifier) == 0 &&
                Objects.equals(name, human.name) &&
                Objects.equals(loc, human.loc) &&
                lastMove == human.lastMove &&
                Objects.equals(dateOfCreation, human.dateOfCreation) &&
                Objects.equals(user, human.user);
    }

    public void hit() {}

    public LocalDateTime getDate() {
        return dateOfCreation;
    }

    public int compareTo(Human human) {
        return human.name.length() - name.length();
    }

    public void setHealth(int health) {
        hp = health;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, loc, hp);
    }

    public double distance(Moveable moveable) {
        return Math.sqrt(Math.pow(getLocation().getY()-moveable.getLocation().getY(), 2.0)
                + Math.pow((getLocation().getX()-moveable.getLocation().getX()), 2.0));
    }


}