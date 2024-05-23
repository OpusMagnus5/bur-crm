package pl.bodzioch.damian.dto;

import java.io.Serializable;
import java.util.List;

public record CreateNewServiceResponse(
        List<String> messages
) implements Serializable {
}
