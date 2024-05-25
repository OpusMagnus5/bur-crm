package pl.bodzioch.damian.dto;

import java.io.Serializable;
import java.util.List;

public record GetAllIntermediariesResponse(
        List<IntermediaryData> intermediaries
) implements Serializable {
}
