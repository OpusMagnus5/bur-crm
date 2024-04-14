package pl.bodzioch.damian.infrastructure.command;

public interface Command<R extends CommandResult> {

    String toString();
}
