package pl.bodzioch.damian.intermediary;

import java.util.Optional;

public record InnerIntermediaryDto(
        Long id,
        String name
) {
    public InnerIntermediaryDto(InnerIntermediary intermediary) {
        this(
                Optional.ofNullable(intermediary).map(InnerIntermediary::id).orElse(null),
                Optional.ofNullable(intermediary).map(InnerIntermediary::name).orElse(null)
        );
    }
}
