package pl.bodzioch.damian.document.command_dto;

import pl.bodzioch.damian.infrastructure.command.Command;

import java.util.List;

public record DeleteDocumentsCommand(
        List<Long> ids
) implements Command<DeleteDocumentsCommandResult> {
}
