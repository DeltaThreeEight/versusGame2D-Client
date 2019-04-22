package Entities;

import World.Location;
import Entities.exceptions.*;

public interface Moveable {
    void move(Moves move) throws NotAliveException;
    Location getLocation();
    double distance(Moveable moveable);
}
