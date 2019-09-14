package Server.Commands;

public interface Command {
    String getCommandName();
    String getArg(int i);
    int getArgsCount();
    String getArgsAsOne();
}
