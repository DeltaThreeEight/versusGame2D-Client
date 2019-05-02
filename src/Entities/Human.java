package Entities;

import Entities.exceptions.NotAliveException;
import GUI.Controllers.HumanController;
import GUI.Main;
import Server.Command;
import ServerCon.ClientCommandHandler;
import World.Location;
import javafx.animation.TranslateTransition;
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
    protected Pane root;

    public Moves getLastMove() {
        return lastMove;
    }

    public Rectangle getCol_rec() {
        return col_rec;
    }

    public void setCol_rec(Rectangle col_rec) {
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
        left_hand = humanController.getLeft_hand();
        right_hand = humanController.getRight_hand();
        head = humanController.getHead();


        Label nm = new Label(getName());
        setOrientation(Orientation.VERTICAL);
        setCol_rec(humanController.getCol_rec());
        getChildren().addAll(nm, root);
        if (getLastMove() == Moves.FORWARD || getLastMove() == Moves.BACK)
            rotare(true);
    }

    public void hide() {
        try {
            ClientCommandHandler.mainWindow.getMainController().getGraphics().getChildren().removeAll(this);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        getChildren().clear();
    }

    public void moveOther(Moves move) {
        if (isAlive()) {
            if (lastMove == Moves.RIGHT || lastMove == Moves.LEFT) {
                if (move == Moves.RIGHT || move == Moves.LEFT) {
                    // do nothing
                } else rotare(true);
            } else {
                if (move == Moves.BACK || move == Moves.FORWARD) {
                    // do nothing
                } else rotare(false);
            }

            lastMove = move;

            setTranslateY(getTranslateY() + move.getY() * speedModifier);
            setTranslateX(getTranslateX() + move.getX() * speedModifier);

            loc.setXY(loc.getX() + move.getX() * speedModifier, loc.getY() + move.getY() * speedModifier);
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
        ClientCommandHandler.dH.executeCommand(new Command("teleport", x+"", y+""));
    }

    public boolean checkIntersects(Moves move) {

        for (Node n : ClientCommandHandler.mainWindow.getMainController().getGraphics().getChildren()) {

            if (n instanceof Human) {

                Human h = (Human) n;

                if (h != this) {
                    if (((Path) Shape.intersect(col_rec, h.getCol_rec())).getElements().size() > 0) {

                        System.out.println("Касание");

                        setTranslateY(getTranslateY() - move.getY() * speedModifier);
                        setTranslateX(getTranslateX() - move.getX() * speedModifier);

                        while (((Path) Shape.intersect(col_rec, h.getCol_rec())).getElements().size() > 0) {
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

    protected void rotare(boolean b) {
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
                } else rotare(true);
            } else {
                if (move == Moves.BACK || move == Moves.FORWARD) {
                    // do nothing
                } else rotare(false);
            }

            lastMove = move;

            setTranslateY(getTranslateY() + move.getY() * speedModifier);
            setTranslateX(getTranslateX() + move.getX() * speedModifier);

            boolean intersects = checkIntersects(move);

            if (!intersects) {
                ClientCommandHandler.dH.executeCommand(new Command("move", move.toString()));
                loc.setXY(loc.getX() + move.getX() * speedModifier, loc.getY() + move.getY() * speedModifier);
                System.out.println("Перемещение " + loc);
            }
        } else System.out.println("Перемещние невозможно");

    }

    public void shootOther() {
        if (ammo > 0) {
            System.out.println("SHOOT");
            final Shape bullet = new Circle(2, Color.ORANGE);
            ClientCommandHandler.mainWindow.getMainController().getGraphics().getChildren().add(bullet);
            final TranslateTransition bulletAnimation = new TranslateTransition(Duration.seconds(2), bullet);

            ((Circle) bullet).setCenterX(getLocation().getX() + 16);
            ((Circle) bullet).setCenterY(getLocation().getY() + 32);

            bulletAnimation.setToX(getLastMove().getX() * 1000);
            bulletAnimation.setToY(getLastMove().getY() * 1000);

            bullet.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
                Pane fr = ClientCommandHandler.mainWindow.getMainController().getGraphics();
                for (Node n : fr.getChildren()) {
                    if (n instanceof Human) {
                        Human h = (Human) n;
                        if (h.getCol_rec() != this.getCol_rec()) {
                            if (((Path) Shape.intersect(bullet, h.getCol_rec())).getElements().size() > 0) {
                                System.out.println("Hit!");
                                h.setHealth(h.getHealth() - 10);
                                if (h == ClientCommandHandler.getPlayerClient()) {
                                    double maxHealt = 0;
                                    if (h instanceof Spy)
                                        maxHealt = 100;
                                    else maxHealt = 150;
                                    ClientCommandHandler.getHpBar().setWidth(56.0 * (((double) h.getHealth()) / maxHealt));
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

            bulletAnimation.setOnFinished(event -> ClientCommandHandler.mainWindow.getMainController().getGraphics().getChildren().remove(bullet));
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
        ClientCommandHandler.getAmmo_amount().setText(ammo+"");
    }

    public void shoot() {
        if (ammo > 0) {
            ammo--;
            ClientCommandHandler.getAmmo_amount().setText(ammo+"");
            ClientCommandHandler.dH.executeCommand(new Command("shoot"));
            System.out.println("SHOOT");
            final Shape bullet = new Circle(2, Color.ORANGE);
            ClientCommandHandler.mainWindow.getMainController().getGraphics().getChildren().add(bullet);
            final TranslateTransition bulletAnimation = new TranslateTransition(Duration.seconds(2), bullet);
            ((Circle) bullet).setCenterX(getLocation().getX() + 16);
            ((Circle) bullet).setCenterY(getLocation().getY() + 32);

            bulletAnimation.setToX(getLastMove().getX() * 1000);
            bulletAnimation.setToY(getLastMove().getY() * 1000);

            bullet.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
                Pane fr = ClientCommandHandler.mainWindow.getMainController().getGraphics();
                for (Node n : fr.getChildren()) {
                    if (n instanceof Human) {
                        Human h = (Human) n;
                        if (h.getCol_rec() != this.getCol_rec()) {
                            if (((Path) Shape.intersect(bullet, h.getCol_rec())).getElements().size() > 0) {
                                System.out.println("Hit!");
                                h.setHealth(h.getHealth() - 10);
                                ClientCommandHandler.dH.executeCommand(new Command("hit", h.getName()));
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

            bulletAnimation.setOnFinished(event -> ClientCommandHandler.mainWindow.getMainController().getGraphics().getChildren().remove(bullet));
            bulletAnimation.play();

        } else {
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