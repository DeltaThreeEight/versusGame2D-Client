package ServerCon;

import Entities.Human;
import Entities.Moves;
import GUI.MainWindow;
import GUI.RegistrationWindow;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.concurrent.Exchanger;

public class ClientReciever extends Thread {


    private ObjectInputStream inputStream;
    private Exchanger<String> exchanger;

    public ClientReciever(ObjectInputStream inStream, Exchanger<String> exchanger) {
        this.exchanger = exchanger;
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
                        System.out.println(respond);
                        String alert = respond;
                        Platform.runLater( () ->  RegistrationWindow.showAlert(alert));
                        break;
                    case "SEND":
                        System.out.println(respond);
                        String add = respond;
                        Platform.runLater( () -> ClientCommandHandler.mainWindow.addToChat(add));
                        break;
                    case "USRPRSN":
                        if (persons == null) {
                            persons = new ArrayList<>();
                        }
                        if (respond.trim().equals("$EOF$")) {
                            ArrayList<String> temp = persons;
                            Platform.runLater( () -> ClientCommandHandler.mainWindow.setUserPersons(temp));
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
                            Platform.runLater( () ->  ClientCommandHandler.mainWindow.showAllPersons(temp));
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
                        ClientCommandHandler.setIsAuth(false);
                        ClientCommandHandler.playerClient = null;
                        ClientCommandHandler.joinedPlayers.clear();
                        Platform.runLater(() -> {
                            ClientCommandHandler.mainWindow.closeMain();
                            RegistrationWindow.showAlert("Прошло более 1.5 минут с последнего запроса вам надо снова авторизоваться");
                        });
                        break;
                    case "DESERIALIZE":
                        try {
                            Human person = (Human) inputStream.readObject();
                            ClientCommandHandler.playerClient = person;
                            addHum(person);
                            Platform.runLater(() -> ClientCommandHandler.mainWindow.setSelectedPerson(person.getName()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
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
                        if (ClientCommandHandler.getIsAuth()) System.out.println(hum.getName()+" зашёл в игру.");
                        break;
                    case "REMPLAYER":
                        try {
                            Human rem = ClientCommandHandler.joinedPlayers.get(respond);
                            Platform.runLater(() -> {
                                ClientCommandHandler.mainWindow.addToChat(rem.getName() + " покинул игру.");
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

    private void addHum(Human human) {
        Platform.runLater( () -> {
            ClientCommandHandler.mainWindow.addPlayer(human);
            human.show();
        });
    }
}
