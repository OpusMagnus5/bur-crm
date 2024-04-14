package pl.bodzioch.damian.user;

import pl.bodzioch.damian.infrastructure.command.Command;

public record CreateNewUserCommand(

        String email,
        String firstName,
        String lastName,
        Long creatorId

) implements Command<CreateNewUserCommandResult>{
}


