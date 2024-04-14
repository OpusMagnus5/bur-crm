package pl.bodzioch.damian.user;

import pl.bodzioch.damian.configuration.command.Command;

public record CreateNewUserCommand(

        String email,
        String firstName,
        String lastName,
        Long creatorId

) implements Command<CreateNewUserCommandResult>{
}


