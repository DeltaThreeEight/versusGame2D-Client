package ServerCon;

import Entities.*;
import Entities.exceptions.NotAliveException;
import GUI.MainWindow;
import com.lambdaworks.codec.Base64;
import com.lambdaworks.crypto.SCrypt;
import com.lambdaworks.crypto.SCryptUtil;

import java.io.*;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.Exchanger;

public class ClientCommandHandler {

    static HashMap<String,Human> joinedPlayers = new HashMap<>();
    public static Human playerClient;
    static String authToken = null;
    public static ClientCommandHandler dH;
    public static MainWindow mainWindow;

    private Exchanger<String> exchanger;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private Scanner scanner;
    private Console console = System.console();
    static boolean isSendingToken = false;
    private static boolean isAuth = false;

    public ClientCommandHandler(ObjectOutputStream writer, ObjectInputStream reader, Exchanger<String> ex) {
        this.writer = writer;
        this.reader = reader;
        exchanger = ex;
        this.scanner = new Scanner(System.in);
        helpUnauthrozied();
        dH = this;
    }

    class Command {
        String name;
        String arguments = null;

        public Command(String str) {
            String[] parts = str.split(" ");
            name = parts[0];
            if (parts.length > 1) arguments = str.replaceFirst(parts[0]+" ", "");
        }

        public String allCmd() {
            if (arguments != null)
            return name + " " + arguments;
            else return name;
        }
        public void setSafe() {
            name = name.replaceAll("\\$", "");
            if (arguments != null) arguments = arguments.replaceAll("\\$", "");
        }
    }

    public static boolean getIsAuth() {
        return isAuth;
    }

    public boolean executeCommand(String cmd) {
        try {
            Command command = new Command(cmd);
            command.setSafe();

            if (isAuth) {
                command.arguments = command.arguments + "$" + authToken;
                switch (command.name) {
                    case "help":
                        sendToServer(command.allCmd());
                        break;
                    case "register":
                        System.out.println("Вы уже авторизованы!");
                        break;
                    case "login":
                        System.out.println("Вы уже авторизованы!");
                        break;
                    case "chat":
                            if (command.arguments == null)
                                System.out.println("Отсутсвуют аргументы\n" +
                                        "Введите команду");
                            else
                                sendToServer(command.allCmd());
                        break;
                    case "select":
                        if (command.arguments == null)
                            System.out.println("Отсутвует аргумент\n" +
                                    "Введите команду");
                        else {
                            sendToServer(command.allCmd());
                        }
                        break;
                    case "showstats":
                        sendToServer(command.allCmd());
                        break;
                    case "remove":
                        if (command.arguments == null)
                            System.out.println("Отсутвует аргумент\n" +
                                    "Введите команду");
                        else {
                            sendToServer(command.allCmd());
                        }
                        break;
                    case "createnew":
                        String parts[] = command.arguments.replace("$"+authToken, "").split(" ");
                        switch (parts[1]) {
                            case "Spy":
                                Spy spy = new Spy(parts[0]);
                                sendToServer("createnew "+parts[0]+"$"+ authToken);
                                sendObject(spy);
                                break;
                            case "Merc":
                                Merc merc = new Merc(parts[0]);
                                sendToServer("createnew "+parts[0]+"$"+ authToken);
                                sendObject(merc);
                                break;
                        }
                        break;
                    case "move":
                        if (command.arguments.replace("$"+ authToken, "").equals(""))
                            System.out.println("Отсутсвует аргумент");
                        else {
                            if (playerClient != null) {
                                Moves move = null;
                                try {
                                    move = Moves.valueOf(command.arguments.replace("$"+ authToken, "").toUpperCase());
                                } catch (Exception e) {
                                }
                                if (move != null) {
                                    try {
                                        playerClient.move(move);
                                        sendToServer(command.allCmd());
                                    } catch (NotAliveException e) {
                                        System.out.println(e.getMessage());
                                    }
                                } else
                                    System.out.println("Неверно указано направление движения");
                            } else System.out.println("Не выбран персонаж");
                        }
                        break;
                    case "exit":
                        sendToServer(command.allCmd());
                        return true;
                    default:
                        sendToServer(command.allCmd());
                }
            } else
                switch (command.name) {
                case "help":
                    helpUnauthrozied();
                    break;
                case "register":
                    System.out.println("Введите имя пользователя");
                    String username = scanner.nextLine().trim().replaceAll("\\s+","");

                    while (username.equals("")) {
                        System.out.println("Имя пользователя не должно быть пустым");
                        username = scanner.nextLine().trim().replaceAll("\\s+","");
                    }

                    System.out.println("Введите почту");
                    String email = scanner.nextLine();

                    System.out.println("Введите пароль");
                    String pass;
                    String passAgain;
                    if (console == null) {
                        pass = scanner.nextLine().trim();

                        while (pass.length() < 4) {
                            System.out.println("Минимальная длина пароля - 4 символов");
                            pass = scanner.nextLine().trim();
                        }
                        System.out.println("Повторите пароль");
                        passAgain = scanner.nextLine().trim();
                    } else {
                        pass = new String(console.readPassword()).trim();

                        while (pass.length() < 4) {
                            System.out.println("Минимальная длина пароля - 4 символов");
                            pass = new String(console.readPassword()).trim();
                        }
                        System.out.println("Повторите пароль");
                        passAgain = new String(console.readPassword()).trim();
                    }

                    boolean match = pass.equals(passAgain);

                    String hash = getHash(pass);

                    if (match) {
                        sendToServer("register "+username+" "+ email +" "+ hash);
                    } else System.out.println("Пароли не совпадают!");

                    break;
                case "login":
                    System.out.println("Введите имя пользователя(Пробелы будут удалены)");
                    String login = scanner.nextLine().trim();

                    while (login.equals("")) {
                        System.out.println("Имя пользователя не должно быть пустым");
                        login = scanner.nextLine().trim();
                    }

                    System.out.println("Введите пароль");
                    String password;
                    if (console != null) password = new String(console.readPassword()).trim();
                    else password = scanner.nextLine().trim();
                    password = getHash(password);
                    sendToServer("login "+login+" "+password);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isSendingToken) {
                        sendToServer(readLine()+"$null");
                        isSendingToken = false;
                    }
                    break;
                case "exit":
                    sendToServer("exit");
                    return true;
                default:
                    System.out.println("Команда не найдена");
                }

            return false;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public String readLine() {
           return scanner.nextLine();
    }

    public Exchanger<String> getExchanger() {
        return exchanger;
    }

    public void sendToServer(String str) {
        try {
            writer.writeUTF(str);
            writer.flush();
        } catch (IOException e) {
            System.out.println("Невозможно отравить запрос серверу.");
        }
    }

    public static void setIsAuth(boolean a) {
        isAuth = a;
    }

    public void sendObject(Object obj) {
        try {
            writer.writeObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void helpUnauthrozied() {
        System.out.println("register - регистрация нового пользователя\n" +
                "login - авторизоваться как уже существующий пользователь");
    }

    public static String getHash(String pass) {
        try {
            byte[] derived = SCrypt.scrypt(pass.getBytes(), "salt".getBytes(), 16, 16, 16, 32);
            return new String(Base64.encode(derived));
        } catch (Exception e) {
            return null;
        }
    }

}
