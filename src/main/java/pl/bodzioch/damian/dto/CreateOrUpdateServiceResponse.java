package pl.bodzioch.damian.dto;

import java.io.Serializable;
import java.util.List;

public record CreateOrUpdateServiceResponse(
        List<String> messages
) implements Serializable {
}
