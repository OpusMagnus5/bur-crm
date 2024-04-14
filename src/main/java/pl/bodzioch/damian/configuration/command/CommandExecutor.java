package pl.bodzioch.damian.configuration.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CommandExecutor {

    private final Map<Class<? extends Command<?>>, CommandHandler<? extends Command<?>, ? extends CommandResult>> handlerMap;

    CommandExecutor(Set<CommandHandler<? extends Command<?>, ?>> handlers) {
        if (CollectionUtils.isEmpty(handlers)) {
            log.warn("There are no command handlers in application context.");
            this.handlerMap = new HashMap<>();
            return;
        }
        handlers.forEach(handler -> log.info("Adding support for command with class {}", handler.commandClass()));
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(CommandHandler::commandClass, Function.identity()));
    }


    @SuppressWarnings("unchecked")
    public <C extends Command<R>, R extends CommandResult> R execute(C command) {
        CommandHandler<C, R> handler = (CommandHandler<C, R>) handlerMap.get(command.getClass());
        log.info("Executing command: {}", command);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CommandStatus commandStatus = CommandStatus.FAIL;
        R result = null;
        try {
            commandStatus = CommandStatus.SUCCESS;
            result = handler.handle(command);
        } catch (Throwable e) {
            log.error("Error occurred while handling command.", e);
            throw e;
        } finally {
            stopWatch.stop();
            log.info("Command with class: {} executed in {}ms with status {} and result: {}",
                    command.getClass().getSimpleName(), stopWatch.getTotalTimeMillis(), commandStatus, result);
        }
        return result;
    }
}
