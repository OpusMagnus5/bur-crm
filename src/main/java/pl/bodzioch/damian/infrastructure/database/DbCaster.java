package pl.bodzioch.damian.infrastructure.database;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.exception.DbCasterException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DbCaster {

    public static final String GENERAL_CURSOR_NAME = "_cursor";

    public static <T> T cast(Class<T> clazz, Object object) {
        try {
            return clazz.cast(object);
        } catch (ClassCastException e) {
            return null;
        }
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T extends Enum> T toEnum(Class<T> clazz, Object dbEnums) {
        String enums = cast(String.class, dbEnums);
        enums = enums == null ? "" : enums;
        return (T) Enum.valueOf(clazz, enums);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> fromProperties(Map<String, Object> properties, Class<T> clazz) {
        try {
            Constructor<T> constructor = (Constructor<T>) getDbConstructor(clazz);
            constructor.setAccessible(true);
            Field[] fields = clazz.getDeclaredFields();
            List<Map<String, Object>> records = (List<Map<String, Object>>) properties.get(GENERAL_CURSOR_NAME);

            List<T> entities = new ArrayList<>();
            for (Map<String, Object> record : records) {
                List<Object> arguments = new ArrayList<>();
                for (Field field : fields) {
                    try {
                        Object objectForField = getObjectForField(record, null, field);
                        arguments.add(objectForField);
                    } catch (DbCasterException ignored) {
                    }
                }
                entities.add(constructor.newInstance(arguments.toArray()));
            }
            return entities;
        } catch (ReflectiveOperationException e) {
            throw AppException.getGeneralError(e);
        }
    }

    private static Object getObjectForField(Map<String, Object> record, String prefix, Field field) throws ReflectiveOperationException {
        List<Annotation> annotations = Arrays.asList(field.getDeclaredAnnotations());
        Optional<DbColumn> dbColumn = findAnnotationType(annotations, DbColumn.class);
        Optional<DbManyToOne> dbManyToOne = findAnnotationType(annotations, DbManyToOne.class);
        if (dbColumn.isPresent()) {
            String columnName = dbColumn.get().name();
            String paramName = StringUtils.isBlank(prefix) ? columnName : prefix + "_" + columnName;
            Object parameter = record.get(paramName);
            return castSimpleObject(parameter, field);
        } else if (dbManyToOne.isPresent()) {
            return extractInnerObject(record, field.getType(), dbManyToOne.get().prefix());
        }
        throw new DbCasterException("This is not annotated field");
    }

    @SuppressWarnings("unchecked")
    private static <T> T extractInnerObject(Map<String, Object> record, Class<T> type, String prefix) throws ReflectiveOperationException {
        Constructor<T> dbConstructor = (Constructor<T>) getDbConstructor(type);
        dbConstructor.setAccessible(true);
        Field[] fields = type.getDeclaredFields();
        List<Object> arguments = new ArrayList<>();
        for (Field field : fields) {
            try {
                Object object = getObjectForField(record, prefix, field);
                arguments.add(object);
            } catch (DbCasterException ignored) {
            }
        }
        return dbConstructor.newInstance(arguments.toArray());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object castSimpleObject(Object parameter, Field field) {
        if (parameter instanceof Timestamp) {
            return ((Timestamp) parameter).toLocalDateTime();
        } else if (field.getType().isInstance(Enum.class)) {
            Class<? extends Enum> enumType = (Class<? extends Enum>) field.getType();
            return toEnum(enumType, parameter);
        } else if (List.class.isAssignableFrom(field.getType())) {
            return getList(parameter, field);
        }
        return parameter;
    }

    @SuppressWarnings({"rawtypes"})
    private static List<? extends Enum> getList(Object parameter, Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
            return getParametrizedObject(typeArguments, parameter);
        }
        throw new DbCasterException("Not implemented type: " + field);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static List<? extends Enum> getParametrizedObject(Type[] typeArguments, Object parameter) {
        if(typeArguments.length == 1 && typeArguments[0] instanceof Class && ((Class<?>) typeArguments[0]).isEnum()) {
            return toEnums(((Class<? extends Enum>) typeArguments[0]), parameter);
        }
        throw new DbCasterException("Not implemented type: " + Arrays.toString(typeArguments));
    }

    private static <T> Constructor<?> getDbConstructor(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(DbConstructor.class))
                .findFirst()
                .orElseThrow(() -> new DbCasterException("The entity constructor should be annotated with @DbConstructor!"));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Annotation> Optional<T> findAnnotationType(List<Annotation> annotations, Class<T> aClazz) {
        return (Optional<T>) annotations.stream()
                .filter(annotation -> annotation.annotationType() == aClazz)
                .findFirst();
    }
}
