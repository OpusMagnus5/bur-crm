package pl.bodzioch.damian.infrastructure.command;

public interface CommandHandler<C extends Command<R>, R extends CommandResult> {

    Class<C> commandClass();

    R handle(C command);
}
