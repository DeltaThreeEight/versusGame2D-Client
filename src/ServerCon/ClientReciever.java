package ServerCon;

import Entities.Human;
import Entities.Moves;
import GUI.Main;
import javafx.application.Platform;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ClientReciever extends Thread {
    private ResourceBundle rb = Main.getMain().getRb();
    private final String AUTH_TOKE_TIMEOUT = rb.getString("auth_token_timeout");
    private final String PLR_JOIN = rb.getString("plr_join");
    private final String PLR_LEFT = rb.getString("plr_left");
    private final String AUTH_SUCCESS = rb.getString("auth_success");
    private final String USER_ALREADY_AUTH = rb.getString("user_already_auth");
    private final String AUTHORIZED = rb.getString("authorized");
    private final String WRONG_LOG_PASS = rb.getString("wrong_log_pass");
    private final String REG_SUCCESS = rb.getString("reg_success");
    private final String NOT_UNIQUE = rb.getString("not_unique");
    private final String NAME = rb.getString("name");
    private final String HEALTH = rb.getString("health");
    private final String CREATION_DATE = rb.getString("creation_date");
    private final String NO_PERSON = rb.getString("no_person");
    private final String PERSON_ALREADY_SELECTED = rb.getString("person_already_selected");
    private final String PERSON_SELECTED = rb.getString("person_selected");
    private final String SAME_NAME = rb.getString("same_name");
    private final String PERSON_REMOVED = rb.getString("person_removed");
    private final String LEFT_SERVER = rb.getString("left_server");
    private final String SERVER_MESSAGE = rb.getString("server_message");

    private ObjectInputStream inputStream;

    public ClientReciever(ObjectInputStream inStream) {
        inputStream = inStream;
    }

    public void run() {
        try {
            String respond;
            String action;
            ArrayList<String> persons = null;
            while (true) {
                String key;
                respond = inputStream.readUTF();
                action = respond.split("\\^")[0];
                respond = respond.replace(action+"^", "");
                switch (action) {
                    case "ALERT":
                        String alert;
                        if (respond.contains(" ")) {
                            String part1 = respond.split(" ")[0];
                            alert = getMsgRes(part1)+ " " + respond.replace(part1+" ", "");
                        } else alert = getMsgRes(respond);
                        Platform.runLater( () ->  Main.showAlert(alert));
                        break;
                    case "SEND":
                        if (respond.contains("SERVER_MESSAGE")) {
                            respond = respond.replace("SERVER_MESSAGE", SERVER_MESSAGE);
                            respond = respond.replace("LEFT_SERVER", LEFT_SERVER);
                            respond = respond.replace("AUTHORIZED", AUTHORIZED);
                        }
                        System.out.println(respond);
                        String add = respond;
                        Platform.runLater( () -> ClientCommandHandler.mainWindow.getMainController().addToChat(add));
                        break;
                    case "USRPRSN":
                        if (persons == null) {
                            persons = new ArrayList<>();
                        }
                        if (respond.trim().equals("$EOF$")) {
                            ArrayList<String> temp = persons;
                            Platform.runLater( () -> ClientCommandHandler.mainWindow.getMainController().setUserPersons(temp));
                            persons = null;
                        } else {
                            persons.add(respond);
                        }
                        break;
                    case "ALLPRSN":
                        if (persons == null) {
                            persons = new ArrayList<>();
                        }
                        if (respond.trim().equals("$EOF$")) {
                            ArrayList<String> temp = persons;
                            Platform.runLater( () ->  ClientCommandHandler.mainWindow.getMainController().showAllPersons(temp));
                            persons = null;
                        } else {
                            persons.add(respond);
                        }
                        break;
                    case "AUTH":
                        ClientCommandHandler.setIsAuth(true);
                        ClientCommandHandler.authToken = respond;
                        break;
                    case "SENDTOKEN":
                        ClientCommandHandler.isSendingToken = true;
                        break;
                    case "DEAUTH":
                        ClientCommandHandler.dH.deauth();
                        Platform.runLater(() -> {
                            ClientCommandHandler.mainWindow.getMainController().closeMain();
                            Main.showAlert(AUTH_TOKE_TIMEOUT);
                        });
                        break;
                    case "DESERIALIZE":
                        try {
                            Human person = (Human) inputStream.readObject();
                            ClientCommandHandler.playerClient = person;
                            addHum(person);
                            Platform.runLater(() -> ClientCommandHandler.mainWindow.getMainController().setSelectedPerson(person.getName()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "STATS":
                        try {
                            Human person = (Human) inputStream.readObject();
                            Platform.runLater(() -> Main.showAlert(String.format
                                    ("%s: %s%n" +
                                    "%s: %s%n" +
                                            "%s: %s", NAME, person.getName(), HEALTH, person.getHealth(), CREATION_DATE, person.getDate())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    case "LOADPLR":
                        key = respond.split("\\^")[0];
                        Human obj;
                        try {
                            obj = (Human) inputStream.readObject();
                        } catch (Exception i) {
                            i.printStackTrace();
                            break;
                        }
                        addHum(obj);
                        ClientCommandHandler.joinedPlayers.put(key, obj);
                        break;
                    case "ADDPLAYER":
                        key = respond.split("\\^")[0];
                        Human hum;
                        try {
                            hum = (Human) inputStream.readObject();
                        } catch (Exception i) {
                            i.printStackTrace();
                            break;
                        }
                        addHum(hum);
                        ClientCommandHandler.joinedPlayers.put(key, hum);
                        if (ClientCommandHandler.getIsAuth())
                            Platform.runLater( () -> ClientCommandHandler.mainWindow.getMainController().addToChat(String
                                    .format("%s %s", hum.getName(), PLR_JOIN)));
                        break;
                    case "REMPLAYER":
                        try {
                            Human rem = ClientCommandHandler.joinedPlayers.get(respond);
                            Platform.runLater(() -> {
                                Platform.runLater( () -> ClientCommandHandler.mainWindow.getMainController().addToChat(String
                                        .format("%s %s", rem.getName(), PLR_LEFT)));
                                rem.hide();
                            });
                            ClientCommandHandler.joinedPlayers.remove(respond);
                        } catch (NullPointerException e) {
                            ClientCommandHandler.playerClient = null;
                        }
                        break;
                    case "MOVPLAYER":
                        Moves move = Moves.valueOf(respond.split("\\^")[0]);
                        respond = respond.replace(move+"^", "");
                        Human player2 = ClientCommandHandler.joinedPlayers.get(respond);
                        ClientCommandHandler.joinedPlayers.values().stream()
                                .filter(c -> player2.getName().equals(c.getName()))
                                .forEach(c -> {c.move(move); if (ClientCommandHandler.getIsAuth()) System.out.println(c.getName() + " переместился: "+c.getLocation().getName());});
                        break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Отключен от сервера.");
            System.exit(0);
        }
    }

    private String getMsgRes(String alert) {
        switch (alert) {
            case "AUTH_SUCCESS":
                return AUTH_SUCCESS;
            case "USER_ALREADY_AUTH":
                return USER_ALREADY_AUTH;
            case "AUTHORIZED":
                return AUTHORIZED;
            case "WRONG_LOG_PASS":
                return WRONG_LOG_PASS;
            case "REG_SUCCESS":
                return REG_SUCCESS;
            case "NOT_UNIQUE":
                return NOT_UNIQUE;
            case "NO_PERSON":
                return NO_PERSON;
            case "PERSON_ALREADY_SELECTED":
                return PERSON_ALREADY_SELECTED;
            case "PERSON_SELECTED":
                return PERSON_SELECTED;
            case "SAME_NAME":
                return SAME_NAME;
            case "PERSON_REMOVED":
                return PERSON_REMOVED;
        }
        return null;
    }

    private void addHum(Human human) {
        Platform.runLater( () -> {
            ClientCommandHandler.mainWindow.getMainController().addPlayer(human);
            human.show();
        });
    }
}
