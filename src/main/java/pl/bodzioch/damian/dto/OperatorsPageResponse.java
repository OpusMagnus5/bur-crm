package pl.bodzioch.damian.dto;

import java.io.Serializable;
import java.util.List;

public record OperatorsPageResponse(
        List<OperatorData> operators,
        Long totalOperators
) implements Serializable {
}
