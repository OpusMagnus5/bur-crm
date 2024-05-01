package pl.bodzioch.damian.service_provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.client.bur.BurServiceProviderDto;
import pl.bodzioch.damian.client.bur.IBurClient;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.service_provider.command_dto.GetProviderNameByNipFromBurCommand;
import pl.bodzioch.damian.service_provider.command_dto.GetProviderNameByNipFromBurCommandResult;

@Component
@RequiredArgsConstructor
class GetProviderNameByNipFromBurCommandHandler implements CommandHandler<GetProviderNameByNipFromBurCommand, GetProviderNameByNipFromBurCommandResult> {

    private final IBurClient burClient;

    @Override
    public Class<GetProviderNameByNipFromBurCommand> commandClass() {
        return GetProviderNameByNipFromBurCommand.class;
    }

    @Override
    public GetProviderNameByNipFromBurCommandResult handle(GetProviderNameByNipFromBurCommand command) {
        String name = burClient.getServiceProvider(Long.parseLong(command.nip()))
                .onErrorComplete()
                .blockOptional()
                .map(BurServiceProviderDto::name)
                .orElse("");
        return new GetProviderNameByNipFromBurCommandResult(name);
    }
}
