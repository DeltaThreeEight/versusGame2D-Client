package Network.Connection;

import Entities.Human;
import Entities.Moves;
import GUI.Main;
import GUI.TokenInputWindow;
import Network.Actions;
import Server.Commands.ClientCommand;
import Server.Commands.ServerResponse;
import Resources.TextResources;
import javafx.application.Platform;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.time.format.DateTimeFormatter;

public class ClientReceiver implements Runnable {

    private ObjectInputStream inputStream;
    private ClientCommandHandler handler;
    private TextResources resources;
    private Main main;

    public ClientReceiver(ObjectInputStream inStream, ClientCommandHandler handler, TextResources resources) {
        this.inputStream = inStream;
        this.handler = handler;
        this.resources = resources;
        this.main = handler.getMain();
    }

    public void run() {
        try {
            Actions act = null;
            while (act != Actions.CLOSECONNECTION) {
                String respond = inputStream.readUTF();

                String action = respond.split("\\^")[0];
                respond = respond.replace(action+"^", "");

                act = Actions.valueOf(action);
                ServerResponse response = new ServerResponse(act, respond);

                handleResponse(response);
            }
        }
        catch (SocketException s) {
            System.err.println("Потеряно соединение с сервером");
            System.exit(0);
        }
        catch (EOFException x) {
            System.out.println("Сервер закрыл соединение");
            System.exit(0);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Отключен от сервера.");
            System.exit(0);
        }
    }

    public boolean handleResponse(ServerResponse response) {
        try {
            switch (response.getAction()) {
                case ALERT:
                    showAlert(response.getData());
                    break;
                case SEND:
                    showInChat(response.getData());
                    break;
                case SENDPERSONLIST:
                    String[] personsList = (String[]) inputStream.readObject();
                    Platform.runLater(() -> setUserPersons(personsList));
                    break;
                case SENDALLPERSONS:
                    String[] allPersons = (String[]) inputStream.readObject();
                    showAllPersons(allPersons);
                    break;
                case LOADPUDDLE:
                    loadPuddle(response.getData());
                    break;
                case REQUESTTOKEN:
                    showTokenInputWindow();
                    break;
                case DEAUTH:
                    Platform.runLater(() -> main.closeReq());
                    break;
                case DESERIALIZE:
                    selectPerson();
                    break;
                case ROTARE:
                    rotarePerson(response.getData());
                    break;
                case STATS:
                    showStats();
                    break;
                case RELOAD:
                    reloadWeapon(response.getData());
                    break;
                case LOADPLAYERS:
                    loadPlayer(response.getData());
                    break;
                case ADDPLAYER:
                    playerJoin(response.getData());
                    break;
                case REMPLAYER:
                    playerLeft(response.getData());
                    break;
                case TELEPORT:
                    teleportPlayer(response.getData());
                    break;
                case SHOOT:
                    shootPlayer(response.getData());
                    break;
                case KILLPLAYER:
                    killPlayer(response.getData());
                    break;
                case MOVPLAYER:
                    movePlayer(response.getData());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void movePlayer(String data) {
        Moves move = Moves.valueOf(data.split("\\^")[0]);

        data = data.replace(move+"^", "");
        Human player = handler.getJoinedPlayers().get(data);

        Platform.runLater(() -> player.moveOther(move));
    }

    private void shootPlayer(String data) {
        Human playerShoot = handler.getJoinedPlayers().get(data);
        Platform.runLater(playerShoot::shootOther);
    }

    private void killPlayer(String data) {
        if (handler.getPlayerClient() != null && data.equals(handler.getPlayerClient().getName())) {
            Platform.runLater( () -> {
                main.showAlert(resources.PERSON_KILLED);

                loadPuddle(handler.getPlayerClient().getLocation().getX(), handler.getPlayerClient().getLocation().getY());

                handler.getMainWindow().getMainController().setSelectedPerson(resources.DEFAULT_PERSON);
                handler.setPlayerNull();
            });
        } else {
            Human killed = handler.getJoinedPlayers().get(data);

            Platform.runLater(() -> {
                loadPuddle(killed.getLocation().getX(), killed.getLocation().getY());
                killed.hide();
            });
        }
    }

    private void teleportPlayer(String data) {
        String name = data.split("\\^")[1];

        String xy = data.replace("^"+name, "");

        double x = Double.parseDouble(xy.split(" ")[0]);
        double y = Double.parseDouble(xy.split(" ")[1]);

        Human person = handler.getJoinedPlayers().get(name);

        handler.getJoinedPlayers().values()
                .stream()
                .filter(c -> person.getName().equals(c.getName()))
                .forEach(c -> c.teleportOther(x, y));
    }

    private void playerLeft(String data) {
        Human rem = handler.getJoinedPlayers().get(data);

        Platform.runLater(() -> {
            if (rem != null) {
                handler.getMainWindow().getMainController().addToChat(String.format(
                        "%s %s%n", rem.getName(), resources.PLAYER_LEFT));
                rem.hide();
            } else {
                handler.getMainWindow().getMainController().setSelectedPerson(resources.DEFAULT_PERSON);
                if (handler.getPlayerClient() != null)
                    handler.setPlayerNull();
            }
        });

        handler.getJoinedPlayers().remove(data);
    }

    private void reloadWeapon(String data) {
        if (handler.getPlayerClient().getName().equals(data)) {
            Platform.runLater(() -> handler.getPlayerClient().reload());
        }
    }

    private void playerJoin(String data) {
        Human person = null;

        try {
            person = (Human) inputStream.readObject();
            person.setHandler(handler);
        } catch (Exception i) {
            i.printStackTrace();
        }

        Human human = person;
        addHum(person);

        handler.getJoinedPlayers().put(human.getName(), person);

        if (handler.getIsAuth())
            Platform.runLater( () -> handler.getMainWindow().getMainController().addToChat(String.format(
                    "%s %s%n", human.getName(), resources.PLAYER_JOIN)));
    }

    private void loadPlayer(String data) {
        String key = data.split("\\^")[0];
        Human person;

        try {
            person = (Human) inputStream.readObject();
            person.setHandler(handler);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        addHum(person);
        handler.getJoinedPlayers().put(key, person);
    }

    private void showStats() {
        try {
            Human person = (Human) inputStream.readObject();

            DateTimeFormatter format = DateTimeFormatter.ofPattern("H:m d MMMM yyyy", main.getLocale());

            Platform.runLater(() -> main.showAlert(String.format(
                    "%s: %s%n" +
                    "%s: %s%n" +
                    "%s: %s",
                    resources.NAME, person.getName(),
                    resources.HEALTH, person.getHealth(),
                    resources.CREATION_DATE, person.getDate().format(format))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rotarePerson(String data) {
        String namePerson = data.split("\\^")[0];
        String move = data.replaceFirst(namePerson+"\\^", "");

        Human person = handler.getJoinedPlayers().get(namePerson);
        Moves moveRotare = Moves.valueOf(move);

        if (moveRotare == Moves.LEFT || moveRotare == Moves.RIGHT)
            person.rotare(false);
        else
            person.rotare(true);

        person.setLastMove(moveRotare);
    }

    private void selectPerson() {
        try {
            Human person = (Human) inputStream.readObject();
            person.setHandler(handler);

            Platform.runLater(() -> {
                handler.setPlayerClient(person);
                addHum(person);
                handler.getMainWindow().getMainController().setSelectedPerson(String.format(resources.PERSON_SELECTED, person.getName()));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addHum(Human human) {
        Platform.runLater( () -> {
            handler.getMainWindow().getMainController().addPlayer(human);
            human.show();
        });
    }

    private void showTokenInputWindow() {
        Platform.runLater(() -> {
            Stage stage = new Stage();

            stage.setOnCloseRequest(event -> handler.sendCMD(new ClientCommand("name", "sdad")));

            TokenInputWindow window = new TokenInputWindow(handler);

            handler.setTokenWindow(stage);

            stage.setScene(window.getScreen());
            stage.show();

            main.showAlert(resources.UNCONF_TOKEN);
        });
    }

    private void loadPuddle(String data) {
        double xp = Double.parseDouble(data.split(" ")[0]);
        double yp = Double.parseDouble(data.split(" ")[1]);

        Platform.runLater(() -> loadPuddle(xp, yp));
    }

    private void showAllPersons(String[] persons) {
        Platform.runLater(() ->  main.getMainController().showAllPersons(persons));
    }

    private void setUserPersons(String[] personsList) {
        handler.getMainWindow().getMainController().setUserPersons(personsList);
    }

    private void showInChat(String data) {
        System.out.println(data);
        String msg = data.replace("SERVER_MESSAGE", resources.SERVER_MESSAGE)
                .replace("PLR_JOIN", resources.PLAYER_JOIN)
                .replace("PLR_LEFT", resources.PLAYER_LEFT)
                .replace("AUTHORIZED", resources.AUTHORIZED);

        Platform.runLater(() -> handler.getMainWindow().getMainController().addToChat(msg));
    }

    private void showAlert(String data) {
        System.out.println(data);
        Platform.runLater(() ->  main.showAlert(selectResource(data)));
    }

    public void loadPuddle(double x, double y) {
        Ellipse ell = new Ellipse();
        ell.setRadiusY(12);
        ell.setRadiusX(15);
        ell.setStroke(Paint.valueOf("BLACK"));
        ell.setFill(Paint.valueOf("#d30000"));
        ell.setCenterX(x+16);
        ell.setCenterY(y+35);
        handler.getMainWindow().getMap().getChildren().addAll(ell);
        ell.toBack();
        handler.getMainWindow().getHouseFloor().toBack();
    }

    private String selectResource(String data) {
        switch (data.split(" ")[0]) {
            case "WRONG_LOG_PASS":
                return resources.WRONG_LOG_PASS;
            case "REG_SUCCESS":
                return resources.REG_SUCCESS;
            case "NOT_UNIQUE":
                return resources.NOT_UNIQUE;
            case "AUTH_SUCCESS":
                return resources.AUTH_SUCCESS;
            case "EMAIL_CONF":
                return resources.EMAIL_CONF;
            case "PERSON_REMOVED":
                return resources.PERSON_REMOVED;
            case "PERSON_SELECTED":
                return String.format(resources.PERSON_SELECTED, data.split(" ")[1]);
            case "USER_ALREADY_AUTH":
                return resources.USER_ALREADY_AUTH;
            case "PERSON_ALREADY_SELECTED":
                return resources.PERSON_ALREADY_SELECTED;
            case "SAME_NAME":
                return resources.SAME_NAME;
            default:
                return data;
        }
    }

}
