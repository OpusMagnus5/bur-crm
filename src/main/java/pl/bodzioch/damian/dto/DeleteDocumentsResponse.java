package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record DeleteDocumentsResponse(
        String message
) implements Serializable {
}
