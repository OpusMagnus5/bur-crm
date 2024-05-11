package pl.bodzioch.damian.dto;

import java.util.List;

public record AllOperatorsResponse(
        List<OperatorData> operators
) {
}
