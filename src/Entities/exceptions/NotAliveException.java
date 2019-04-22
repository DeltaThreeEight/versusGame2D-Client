package Entities.exceptions;

import Entities.Human;

public class NotAliveException extends RuntimeException {//Unchecked exception
    private Human creature; //Объект пытавшийся переместиться

    public NotAliveException(Human human) {
        super(human.getName() + " мертв.");
        this.creature = creature;
    }

    public Human getNotAliveCreature() {
        return creature;
    }
}
