package pl.bodzioch.damian.configuration.command;

public interface Command<R extends CommandResult> {

    String toString();
}
