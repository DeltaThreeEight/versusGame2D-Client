package ServerCon;

import Entities.Human;
import Entities.Moves;
import GUI.Main;
import GUI.TokenInputWindow;
import Server.Command;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ClientReciever extends Thread {
    private ResourceBundle rb = Main.getMain().getRb();
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
    private final String SEL_PERSON = rb.getString("sel_person");
    private final String SAME_NAME = rb.getString("same_name");
    private final String PERSON_REMOVED = rb.getString("person_removed");
    private final String LEFT_SERVER = rb.getString("left_server");
    private final String SERVER_MESSAGE = rb.getString("server_message");
    private final String EXPIRED_TOKEN = rb.getString("expired_token");
    private final String EXPIRED_REGISTRATION_TOKEN = rb.getString("expired_registration_token");
    private final String EMAIL_CONF = rb.getString("email_conf");
    private final String WRONG_TOKEN = rb.getString("wrong_token");
    private final String UNCONF_TOKEN = rb.getString("unconf_token");
    private final String DEF_PERSON = rb.getString("def_prsn");

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
                        System.out.println(respond);
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
                            Platform.runLater( () -> {
                                Stage crt_window = ClientCommandHandler.mainWindow.getMainController().getCreationWindow();
                                if (crt_window != null) {
                                    crt_window.hide();
                                }
                                ClientCommandHandler.mainWindow.getMainController().setUserPersons(temp);
                            });
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
                        Platform.runLater(() -> {
                            Stage stage = new Stage();
                            stage.setOnCloseRequest(event -> ClientCommandHandler.dH.sendCMD(new Command("sddsd", "sdad")));
                            TokenInputWindow window = new TokenInputWindow();
                            ClientCommandHandler.dH.setToken_window(stage);
                            stage.setScene(window.getScen());
                            stage.show();
                            Main.showAlert(getMsgRes("UNCONF_TOKEN"));
                        });

                        break;
                    case "DEAUTH":
                        Platform.runLater(() -> Main.closeReq());
                        break;
                    case "DESERIALIZE":
                        try {
                            Human person = (Human) inputStream.readObject();
                            Platform.runLater(() -> {
                                ClientCommandHandler.playerClient = person;
                                addHum(person);
                                ClientCommandHandler.mainWindow.getMainController().setSelectedPerson(String.format(SEL_PERSON, person.getName()));
                            });
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
                                    .format("%s %s%n", hum.getName(), PLR_JOIN)));
                        break;
                    case "REMPLAYER":
                        Pane graphics = ClientCommandHandler.mainWindow.getMainController().getGraphics();
                            Human rem = ClientCommandHandler.joinedPlayers.get(respond);
                            Platform.runLater(() -> {
                                try {
                                    ClientCommandHandler.mainWindow.getMainController().addToChat(String
                                            .format("%s %s%n", rem.getName(), PLR_LEFT));
                                    rem.hide();
                                    graphics.getChildren().removeAll(rem);
                                } catch (NullPointerException e) {
                                    ClientCommandHandler.mainWindow.getMainController().setSelectedPerson(DEF_PERSON);
                                    if (ClientCommandHandler.playerClient != null) {
                                        ClientCommandHandler.playerClient.hide();
                                        graphics.getChildren().removeAll(ClientCommandHandler.playerClient);
                                    }
                                    ClientCommandHandler.playerClient = null;
                                }
                            });
                            ClientCommandHandler.joinedPlayers.remove(respond);
                        break;
                    case "TELEPORT":
                        String name1 = respond.split("\\^")[1];
                        String xy = respond.replace("^"+name1, "");
                        double x = Double.parseDouble(xy.split(" ")[0]);
                        double y = Double.parseDouble(xy.split(" ")[1]);

                        Human player3 = ClientCommandHandler.joinedPlayers.get(name1);
                        ClientCommandHandler.joinedPlayers.values().stream()
                                .filter(c -> player3.getName().equals(c.getName()))
                                .forEach(c -> c.teleportOther(x, y));
                        break;
                    case "MOVPLAYER":
                        Moves move = Moves.valueOf(respond.split("\\^")[0]);
                        respond = respond.replace(move+"^", "");
                        Human player2 = ClientCommandHandler.joinedPlayers.get(respond);
                        ClientCommandHandler.joinedPlayers.values().stream()
                                .filter(c -> player2.getName().equals(c.getName()))
                                .forEach(c -> c.moveOther(move));
                        break;
                }
            }
        }
        catch (SocketException s) {
            System.out.println("Потеряно соединение с сервером");
            System.exit(0);
        }
        catch (EOFException x) {
            System.out.println("Сервер закрыл соединение");
            System.exit(0);
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
            case "EXPIRED_TOKEN":
                return EXPIRED_TOKEN;
            case "EXPIRED_REGISTRATION_TOKEN":
                return EXPIRED_REGISTRATION_TOKEN;
            case "EMAIL_CONF":
                return EMAIL_CONF;
            case "WRONG_TOKEN":
                return WRONG_TOKEN;
            case "UNCONF_TOKEN":
                return UNCONF_TOKEN;
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
