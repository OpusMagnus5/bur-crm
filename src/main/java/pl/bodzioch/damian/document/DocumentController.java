package pl.bodzioch.damian.document;

import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.bodzioch.damian.document.command_dto.AddNewDocumentsCommand;
import pl.bodzioch.damian.document.command_dto.AddNewDocumentsCommandData;
import pl.bodzioch.damian.document.command_dto.AddNewDocumentsCommandResult;
import pl.bodzioch.damian.dto.AddNewFilesResponse;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.utils.CipherComponent;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/document")
@RequiredArgsConstructor
@Validated
class DocumentController {

    private final CommandExecutor commandExecutor;
    private final QueryExecutor queryExecutor;
    private final CipherComponent cipher;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    AddNewFilesResponse addNewFiles(@RequestParam
                                    @NotEmpty(message = "error.client.document.filesEmpty") //TODO walidator rozszerzenia i typu pliku
                                    List<MultipartFile> files,
                                    @RequestParam
                                    @NotEmpty(message = "error.client.document.fileTypeEmpty")
                                    String fileType,
                                    @RequestParam
                                    @NotEmpty(message = "error.client.document.serviceIdEmpty")
                                    String serviceId,
                                    @RequestParam(required = false)
                                    String coachId
    ) {
        List<AddNewDocumentsCommandData> commandData = files.stream()
                .map(item -> buildCommandData(fileType, serviceId, coachId, item))
                .toList();
        AddNewDocumentsCommand command = new AddNewDocumentsCommand(commandData);
        AddNewDocumentsCommandResult result = commandExecutor.execute(command);
        return new AddNewFilesResponse(result.message());
    }

    private AddNewDocumentsCommandData buildCommandData(String fileType, String serviceId, String coachId, MultipartFile item) {
        try {
            return new AddNewDocumentsCommandData(item, fileType, serviceId, coachId, 1L, cipher);
        } catch (IOException e) {
            throw AppException.getGeneralError(e);
        }
    }
}
