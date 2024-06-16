package pl.bodzioch.damian.document;

import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.bodzioch.damian.document.command_dto.*;
import pl.bodzioch.damian.document.query_dto.GetDocumentsQuery;
import pl.bodzioch.damian.document.query_dto.GetDocumentsQueryResult;
import pl.bodzioch.damian.document.validator.DocumentTypeV;
import pl.bodzioch.damian.document.validator.FileListExtensionV;
import pl.bodzioch.damian.dto.AddNewFilesResponse;
import pl.bodzioch.damian.dto.DeleteDocumentsResponse;
import pl.bodzioch.damian.dto.DocumentTypeData;
import pl.bodzioch.damian.dto.GetAllDocumentTypesResponse;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.utils.CipherComponent;
import pl.bodzioch.damian.utils.MessageResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/document")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "*", exposedHeaders = "Content-Disposition")
class DocumentController {

    private final CommandExecutor commandExecutor;
    private final QueryExecutor queryExecutor;
    private final CipherComponent cipher;
    private final MessageResolver messageResolver;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    AddNewFilesResponse addNewFiles(@RequestParam
                                    @NotEmpty(message = "error.client.document.filesEmpty")
                                    @FileListExtensionV(extensions = { "pdf" }, message = "error.client.document.incorrectFileExtension")
                                    List<MultipartFile> files,
                                    @RequestParam
                                    @NotEmpty(message = "error.client.document.fileTypeEmpty")
                                    @DocumentTypeV(message = "error.client.document.incorrectFileType")
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

    @GetMapping("/types")
    @ResponseStatus(HttpStatus.OK)
    GetAllDocumentTypesResponse getAllTypes() {
        List<DocumentTypeData> documentTypeData = Arrays.stream(DocumentType.values())
                .map(item -> new DocumentTypeData(item, messageResolver))
                .toList();
        return new GetAllDocumentTypesResponse(documentTypeData);
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<Resource> getDocuments(@RequestParam
                                          @NotEmpty(message = "error.client.document.idsListEmpty")
                                          List<String> ids
    ) {
        List<Long> documentIds = ids.stream()
                .map(cipher::decryptMessage)
                .map(Long::parseLong)
                .toList();
        GetDocumentsQuery query = new GetDocumentsQuery(documentIds);
        GetDocumentsQueryResult result = queryExecutor.execute(query);
        ByteArrayResource byteArrayResource = new ByteArrayResource(result.zipFileData());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + result.name());

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(byteArrayResource);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    DeleteDocumentsResponse deleteDocuments(@RequestParam
                                            @NotEmpty(message = "error.client.document.idsListEmpty")
                                            List<String> ids
    ) {
        List<Long> documentIds = ids.stream()
                .map(cipher::decryptMessage)
                .map(Long::parseLong)
                .toList();
        DeleteDocumentsCommand command = new DeleteDocumentsCommand(documentIds);
        DeleteDocumentsCommandResult result = commandExecutor.execute(command);
        return new DeleteDocumentsResponse(result.message());
    }

    private AddNewDocumentsCommandData buildCommandData(String fileType, String serviceId, String coachId, MultipartFile item) {
        try {
            return new AddNewDocumentsCommandData(item, fileType, serviceId, coachId, 1L, cipher);
        } catch (IOException e) {
            throw AppException.getGeneralError(e);
        }
    }
}
