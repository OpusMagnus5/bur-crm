package pl.bodzioch.damian.configuration.user;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.user.UserRole;
import pl.bodzioch.damian.user.command_dto.CreateSystemUserCommand;

import java.util.List;

@EnableConfigurationProperties(SystemUsersCredentials.class)
@Configuration
class UserConfiguration {

    private final List<CreateSystemUserCommand> systemUsers;
    private final CommandExecutor commandExecutor;
    private final SystemUsersCredentials systemUsersCredentials;

    UserConfiguration(CommandExecutor commandExecutor, SystemUsersCredentials systemUsersCredentials) {
        this.commandExecutor = commandExecutor;
        this.systemUsersCredentials = systemUsersCredentials;
        this.systemUsers = List.of(
                new CreateSystemUserCommand(-1L, "", "1@burcrm.pl", "Administrator", "Systemu", UserRole.ADMIN, -1L),
                new CreateSystemUserCommand(-2L,"", "2@burcrm.pl", "Synchronizator", "Statusu", UserRole.SYSTEM_USER, -1L)
        );
    }

    @EventListener(ApplicationReadyEvent.class)
    public void setUsers() {

    }

}
