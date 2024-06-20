package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record ErrorDto(
        String code,
        String detail
) implements Serializable {
}
