package pl.bodzioch.damian.dto;

import java.io.Serializable;
import java.util.List;

public record IntermediaryPageResponse(
        List<IntermediaryData> intermediaries,
        Long totalIntermediaries
) implements Serializable {
}
