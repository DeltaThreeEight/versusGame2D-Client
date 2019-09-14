package Server.Commands;

import Entities.Human;
import Network.Actions;

public class ServerResponse {

    private Actions action;
    private String data;
    private Human person;

    public ServerResponse(Actions action, String data) {
        this.action = action;
        this.data = data;
    }

    public Actions getAction() {
        return action;
    }

    public void setPerson(Human person) {
        this.person = person;
    }

    public String getData() {
        return data;
    }
}
