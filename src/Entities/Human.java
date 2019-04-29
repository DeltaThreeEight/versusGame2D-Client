package Entities;

import Entities.exceptions.NotAliveException;
import Server.Command;
import ServerCon.ClientCommandHandler;
import World.Location;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Human extends FlowPane implements Moveable, Comparable<Human>, Serializable {
    private String name;
    private Location loc;
    private int hp = 100;
    private Moves lastMove = Moves.BACK;
    private LocalDateTime dateOfCreation;
    private double speedModifier = 1.0;
    private String user = "default";
    private Rectangle col_rec;

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

    }

    public void hide() {
        getChildren().clear();
    }

    public void moveOther(Moves move) {
        checkAlive();

        lastMove = move;

        setTranslateY(getTranslateY() + move.getY()*speedModifier);
        setTranslateX(getTranslateX() + move.getX()*speedModifier);

        loc.setXY(loc.getX()+ move.getX()*speedModifier, loc.getY() + move.getY()*speedModifier);
    }

    public boolean checkIntersects(Human h) {
        if (((Path) Shape.intersect(col_rec, h.col_rec)).getElements().size() > 0) return true;
        else return false;
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

    public void move(Moves move) throws NotAliveException {

        checkAlive();

        lastMove = move;

        setTranslateY(getTranslateY() + move.getY()*speedModifier);
        setTranslateX(getTranslateX() + move.getX()*speedModifier);

        boolean intersects = false;
        for (Node n : ClientCommandHandler.mainWindow.getMainController().getGraphics().getChildren()) {
            if (n instanceof Human) {
                Human h = (Human) n;
                if (h.col_rec != this.col_rec) {
                    if (checkIntersects(h)) {
                        intersects = true;
                        System.out.println("Касание");
                        setTranslateY(getTranslateY() - move.getY()*speedModifier);
                        setTranslateX(getTranslateX() - move.getX()*speedModifier);
                        while (checkIntersects(h)) {
                            teleport(loc.getX() + 10, loc.getY() + 10);
                        }
                        break;
                    }
                }
            }
        }

        if (!intersects) {
            ClientCommandHandler.dH.executeCommand(new Command("move", move.toString()));
            loc.setXY(loc.getX()+ move.getX()*speedModifier, loc.getY() + move.getY()*speedModifier);
            System.out.println("Перемещение "+loc);
        }


    }

    public abstract void shoot();

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

    public void checkAlive() throws NotAliveException{
        if (hp < 1) throw new NotAliveException(this);
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