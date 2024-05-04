package pl.bodzioch.damian.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DbCaster {

    public static <T> T cast(Class<T> clazz, Object object) {
        try {
            return clazz.cast(object);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public static <T extends Enum<?>> String enumToDb(T enumeration) {
        return enumeration.name();
    }

    public static String enumsToDb(List<? extends Enum<?>> enums) {
        return enums.stream()
                .map(Enum::name)
                .collect(Collectors.joining(";"));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T extends Enum> List<T> toEnums(Class<T> clazz, Object dbEnums) {
        String enums = cast(String.class, dbEnums);
        enums = enums == null ? "" : enums;
        return (List<T>) Arrays.stream(enums.split(";"))
                .map(value -> Enum.valueOf(clazz, value))
                .toList();
    }

    public static LocalDateTime mapTimestamp(Object object) {
        try {
            Timestamp timestamp = (Timestamp) object;
            return timestamp == null ? null : timestamp.toLocalDateTime();
        } catch (ClassCastException e) {
            return null;
        }
    }
    @SuppressWarnings("unchecked")
    public static <T> List<T> fromProperties(Function<Map<String, Object>, T> mapper, Map<String, Object> properties, String cursorName) {
        ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) properties.get(cursorName);
        ArrayList<T> entities = new ArrayList<>();
        for (Map<String, Object> record : list) {
            T entity = mapper.apply(record);
            entities.add(entity);
        }
        return entities;
    }

}
