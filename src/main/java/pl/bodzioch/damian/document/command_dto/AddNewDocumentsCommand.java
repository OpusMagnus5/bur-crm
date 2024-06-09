package pl.bodzioch.damian.document.command_dto;

import pl.bodzioch.damian.infrastructure.command.Command;

import java.util.List;

public record AddNewDocumentsCommand(
        List<AddNewDocumentsCommandData> documents
) implements Command<AddNewDocumentsCommandResult> {
}
