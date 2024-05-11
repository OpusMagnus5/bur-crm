package pl.bodzioch.damian.operator;

import java.util.Optional;

public record InnerOperatorDto(
        String name
) {

    public InnerOperatorDto(InnerOperator operator) {
        this(
                Optional.ofNullable(operator).map(InnerOperator::name).orElse(null)
        );
    }
}
