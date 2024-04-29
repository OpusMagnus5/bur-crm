package pl.bodzioch.damian.value_object;

import java.util.List;

public record PageQueryResult<T>(

        List<T> elements,
        long totalElements
) {
}
