package pl.bodzioch.damian.valueobject;

import java.util.List;

public record PageQueryResult<T>(

        List<T> elements,
        long totalElements
) {
}
