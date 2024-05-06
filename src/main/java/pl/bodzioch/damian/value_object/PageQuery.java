package pl.bodzioch.damian.value_object;

import pl.bodzioch.damian.utils.FilterField;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record PageQuery(

        int pageNumber,
        int pageSize,
        Map<? extends FilterField, ?> filters
) {

    public PageQuery(int pageNumber, int pageSize) {
        this(pageNumber, pageSize, new HashMap<>());
    }

    public int getFirstResult() {
        return pageSize * pageNumber - pageSize;
    }

    public int getMaxResult() {
        return pageNumber * pageSize;
    }

    public Map<String, Object> toDbProperties() {
        HashMap<String, Object> dbProperties = new HashMap<>();
        Set<? extends FilterField> keys = new HashSet<>(filters.keySet());
        keys.forEach(key -> {
            Object removed = filters.remove(key);
            dbProperties.put(key.dbName(), removed);
        });
        return dbProperties;
    }
}
