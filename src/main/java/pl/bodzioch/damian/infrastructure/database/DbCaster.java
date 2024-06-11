package pl.bodzioch.damian.infrastructure.database;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.exception.DbCasterException;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Slf4j
public class DbCaster {

    public static final String GENERAL_CURSOR_NAME = "_cursor";
    public static final String PROPERTIES_PREFIX = "_";

    @SuppressWarnings("unchecked")
    public static <T> List<T> fromProperties(Map<String, Object> properties, Class<T> clazz) {
        try {
            List<Map<String, Object>> records = getRecordsFromCursor(properties);
            Constructor<T> constructor = (Constructor<T>) getDbConstructor(clazz);
            Field[] fields = clazz.getDeclaredFields();
            String idColumnName = getIdColumnName(fields);

            List<T> entities = new ArrayList<>();
            Set<Object> entityIds = new HashSet<>();

            Iterator<Map<String, Object>> iterator = records.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> record = iterator.next();
                Object id = record.get(idColumnName);
                if (!entityIds.add(id)) {
                    iterator.remove();
                    continue;
                }
                List<Object> arguments = getArgumentsForRecord(fields, record, records, idColumnName);
                T instance = constructor.newInstance(arguments.toArray());
                entities.add(instance);
            }

            return entities;
        } catch (Exception e) {
            log.error("Error while casting entity from DB", e);
            throw AppException.getGeneralError(e);
        }
    }

    public static Map<String, Object> toProperties(Object object) {
        HashMap<String, Object> properties = new HashMap<>();
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            DbColumn dbColumn = field.getDeclaredAnnotation(DbColumn.class);
            if (dbColumn == null) {
                continue;
            }
            String columnName = dbColumn.name();
            String key = PROPERTIES_PREFIX + columnName;
            Object value = getValue(object, field);
            properties.put(key, value);
        }
        return properties;
    }

    public static String toArrayOfCustomTypes(List<String> params, List<?> objects) {
        try {
            StringBuilder builder = new StringBuilder("{\"");

            for(int i = 0; i < objects.size(); i++) {
                Object object = objects.get(i);
                writeStartObjectTag(i, builder);

                for(int j = 0; j < params.size(); j++) {
                    writeParamsSplitter(j, builder);
                    String finalParam = getCustomTypeParamName(params, j);
                    Field field = getFieldOfDbCasterName(object, finalParam);
                    field.setAccessible(true);
                    Object fieldValue = field.get(object);
                    writeValue(fieldValue, builder);
                }

                writeEndObjectTag(builder);
            }
            builder.append("}");
            return builder.toString();
        } catch (IllegalAccessException e) {
            log.error("Error while making array of custom types", e);
            throw new DbCasterException("Error while making array of custom types", e);
        }
    }

    private static void writeStartObjectTag(int i, StringBuilder builder) {
        if (i == 0) {
            builder.append("(");
        } else {
            builder.append(",\"(");
        }
    }

    private static void writeParamsSplitter(int j, StringBuilder builder) {
        if (j != 0) {
            builder.append(",");
        }
    }

    private static String getCustomTypeParamName(List<String> params, int j) {
        String param = params.get(j);
        if (param.startsWith(PROPERTIES_PREFIX))
            param = param.substring(1);
        return param;
    }

    private static Field getFieldOfDbCasterName(Object object, String finalParam) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                .filter(item -> item.isAnnotationPresent(DbColumn.class))
                .filter(item -> item.getDeclaredAnnotation(DbColumn.class).name().equals(finalParam))
                .findAny()
                .orElseThrow(() -> new DbCasterException("There should be field with @DbColumn name = " + finalParam));
    }

    private static void writeEndObjectTag(StringBuilder builder) {
        builder.append(")\"");
    }

    private static void writeValue(Object fieldValue, StringBuilder builder) {
        if (fieldValue instanceof String) {
            builder.append("\\\"");
            builder.append(fieldValue);
            builder.append("\\\"");
        } else if (fieldValue == null) {
        } else {
            builder.append(fieldValue);
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> getRecordsFromCursor(Map<String, Object> properties) {
        return (List<Map<String, Object>>) properties.get(GENERAL_CURSOR_NAME);
    }

    private static List<Object> getArgumentsForRecord(
            Field[] fields,
            Map<String, Object> record,
            List<Map<String, Object>> records,
            String idColumnName
    ) throws ExecutionException, InterruptedException {
        List<Object> arguments = new ArrayList<>();
        List<Future<Object>> futures = new ArrayList<>();
        try(ExecutorService executor = Executors.newCachedThreadPool()) {
            for (Field field : fields) {
                Future<Object> future = executor.submit(() -> getObjectForField(record, null, field, records, idColumnName));
                futures.add(future);
            }
            for (Future<Object> element : futures) {
                arguments.add(element.get());
            }
            return arguments;
        } catch (DbCasterException ignored) {
            return arguments;
        }
    }

    private static Object getValue(Object object, Field field) {
        try {
            if (field.getType().isInstance(Enum.class)) {
                return enumsToDb(List.of((Enum<?>) field.get(object)));
            } else if (List.class.isAssignableFrom(field.getType())) {
                return mapListToDb(field, object);
            } else {
                return field.get(object);
            }
        }  catch (IllegalAccessException e) {
            throw AppException.getGeneralError(e);
        }
    }

    @SuppressWarnings({ "unchecked" })
    private static Object mapListToDb(Field field, Object object) throws IllegalAccessException {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
            if(typeArguments.length == 1 && typeArguments[0] instanceof Class && ((Class<?>) typeArguments[0]).isEnum()) {
                return enumsToDb((List<? extends Enum<?>>) field.get(object));
            } else if (typeArguments.length == 1 && typeArguments[0] instanceof Class){
                return toPrimitiveObjectList(field, object, typeArguments);
            }
        }
        throw new DbCasterException("Not implemented type, field: " + field + " object: " + object);
    }

    @SuppressWarnings("rawtypes")
    private static Object toPrimitiveObjectList(Field field, Object object, Type[] typeArguments) throws IllegalAccessException {
        List list = (List) field.get(object);
        if (list == null) {
            return Array.newInstance((Class<?>) typeArguments[0], 0);
        }
        int size = list.size();
        Object array = Array.newInstance((Class<?>) typeArguments[0], size);
        for (int i = 0; i < size; i++) {
            Array.set(array, i, list.get(i));
        }
        return array;
    }

    private static Object getObjectForField(
            Map<String, Object> record,
            String prefix,
            Field field,
            List<Map<String, Object>> records,
            String idColumnName
    ) throws ReflectiveOperationException {
        List<Annotation> annotations = Arrays.asList(field.getDeclaredAnnotations());
        Optional<DbColumn> dbColumn = findAnnotationType(annotations, DbColumn.class);
        Optional<DbManyToOne> dbManyToOne = findAnnotationType(annotations, DbManyToOne.class);
        Optional<DbOneToMany> dbOneToMany = findAnnotationType(annotations, DbOneToMany.class);
        if (dbColumn.isPresent()) {
            return handleDbColumn(record, prefix, field, records, dbColumn.get(), idColumnName);
        } else if (dbManyToOne.isPresent()) {
            return handleDbManyToOne(record, prefix, field, records, dbManyToOne.get());
        } else if (dbOneToMany.isPresent()) {
            return handleDbOneToMany(record, prefix, field, records, idColumnName, dbOneToMany.get());
        }
        throw new DbCasterException("This is not annotated field");
    }

    @SuppressWarnings("unchecked")
    private static List<Object> handleDbOneToMany(
            Map<String, Object> record,
            String prefix, Field field,
            List<Map<String, Object>> records,
            String idColumnName, DbOneToMany dbOneToMany
    ) {
        String innerPrefix = getInnerPrefix(prefix, dbOneToMany);
        Class<Object> listType = (Class<Object>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        String typeIdColumnName = getIdColumnName(listType.getDeclaredFields());
        Object parentId = record.get(idColumnName);
        Set<Object> ids = new HashSet<>();

        List<Map<String, Object>> filteredRecords = records.stream()
                .filter(el -> parentId.equals(record.get(idColumnName)))
                .filter(el -> {
                    Object id = el.get(innerPrefix + PROPERTIES_PREFIX + typeIdColumnName);
                    return id != null && ids.add(id);
                })
                .toList();
        return filteredRecords.parallelStream()
                .map(el -> extractInnerObject(el, listType, innerPrefix, records))
                .toList();
    }

    private static String getInnerPrefix(String prefix, DbOneToMany dbOneToMany) {
        String innerPrefix;
        if (StringUtils.isNotBlank(prefix)) {
            innerPrefix = prefix + PROPERTIES_PREFIX + dbOneToMany.prefix();
        } else {
            innerPrefix = dbOneToMany.prefix();
        }
        return innerPrefix;
    }

    private static Object handleDbManyToOne(
            Map<String, Object> record,
            String prefix, Field field,
            List<Map<String, Object>> records,
            DbManyToOne dbManyToOne) {
        String innerPrefix = getInnerPrefix(prefix, dbManyToOne);
        return extractInnerObject(record, field.getType(), innerPrefix, records);
    }

    @SuppressWarnings("unchecked")
    private static <T> T extractInnerObject(
            Map<String, Object> record,
            Class<T> type,
            String prefix,
            List<Map<String, Object>> records
    ){
        try {
            Constructor<T> dbConstructor = (Constructor<T>) getDbConstructor(type);
            dbConstructor.setAccessible(true);
            Field[] fields = type.getDeclaredFields();
            String idColumnName = getIdColumnName(fields);
            List<Object> arguments = new ArrayList<>();
            for (Field field : fields) {
                try {
                    Object object = getObjectForField(record, prefix, field, records, idColumnName);
                    arguments.add(object);
                } catch (DbCasterException ignored) {
                }
            }
            return dbConstructor.newInstance(arguments.toArray());
        } catch (ReflectiveOperationException e) {
            throw new DbCasterException("Error occurred while used creating object through reflection!", e);
        }

    }

    private static String getInnerPrefix(String prefix, DbManyToOne dbManyToOne) {
        String innerPrefix;
        if (StringUtils.isNotBlank(prefix)) {
            innerPrefix = prefix + PROPERTIES_PREFIX + dbManyToOne.prefix();
        } else {
            innerPrefix = dbManyToOne.prefix();
        }
        return innerPrefix;
    }

    private static Object handleDbColumn(
            Map<String, Object> record,
            String prefix, Field field,
            List<Map<String, Object>> records,
            DbColumn dbColumn,
            String idColumnName
    ) {
        String columnName = dbColumn.name();
        String paramName = StringUtils.isBlank(prefix) ? columnName : prefix + "_" + columnName;
        Object parameter = record.get(paramName);
        return castSimpleObject(parameter, field, records, idColumnName, record, columnName);
    }

    private static String enumsToDb(List<? extends Enum<?>> enums) {
        return enums.stream()
                .map(Enum::name)
                .collect(Collectors.joining(";"));
    }

    private static String getIdColumnName(Field[] fields) {
        return Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(DbId.class))
                .map(field -> field.getDeclaredAnnotation(DbColumn.class))
                .map(DbColumn::name)
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Entity should have field with @DbId Annotation");
                    return new DbCasterException("Entity should have field with @DbId Annotation");
                });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object castSimpleObject(
            Object parameter,
            Field field,
            List<Map<String, Object>> records,
            String idColumnName,
            Map<String, Object> record,
            String columnName
    ) {
        if (parameter instanceof Timestamp) {
            return ((Timestamp) parameter).toLocalDateTime();
        } else if (parameter instanceof java.sql.Date) {
            return ((java.sql.Date) parameter).toLocalDate();
        } else if (field.getType().isEnum()) {
            Class<? extends Enum> enumType = (Class<? extends Enum>) field.getType();
            return toEnum(enumType, parameter);
        } else if (List.class.isAssignableFrom(field.getType())) {
            return getList(parameter, field, records, idColumnName, record, columnName);
        }
        return parameter;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <T extends Enum> T toEnum(Class<T> clazz, Object dbEnums) {
        String enums = castOrNull(String.class, dbEnums);
        if (enums == null) {
            return null;
        }
        return (T) Enum.valueOf(clazz, enums);
    }

    private static List<?> getList(
            Object parameter,
            Field field,
            List<Map<String, Object>> records,
            String idColumnName,
            Map<String, Object> record,
            String columnName
    ) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
            return getParametrizedObject(typeArguments, parameter, records, idColumnName, record, columnName);
        }
        throw new DbCasterException("Not implemented type: " + field);
    }

    private static <T> T castOrNull(Class<T> clazz, Object object) {
        try {
            return clazz.cast(object);
        } catch (ClassCastException e) {
            return null;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static List<?> getParametrizedObject(
            Type[] typeArguments,
            Object parameter,
            List<Map<String, Object>> records,
            String idColumnName,
            Map<String, Object> record,
            String columnName
    ) {
        if(typeArguments.length == 1 && typeArguments[0] instanceof Class && ((Class<?>) typeArguments[0]).isEnum()) {
            return toEnums(((Class<? extends Enum>) typeArguments[0]), parameter);
        } else if (typeArguments.length == 1 && typeArguments[0] instanceof Class) {
            return getPrimitiveObjectList(records, idColumnName, record, columnName);
        }
        throw new DbCasterException("Not implemented type: " + Arrays.toString(typeArguments));
    }

    private static <T> Constructor<?> getDbConstructor(Class<T> clazz) {
        Constructor<?> constr = Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(DbConstructor.class))
                .findFirst()
                .orElseThrow(() -> new DbCasterException("The entity constructor should be annotated with @DbConstructor!"));
        constr.setAccessible(true);
        return constr;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <T extends Enum> List<T> toEnums(Class<T> clazz, Object dbEnums) {
        String enums = castOrNull(String.class, dbEnums);
        enums = enums == null ? "" : enums;
        return (List<T>) Arrays.stream(enums.split(";"))
                .map(value -> Enum.valueOf(clazz, value))
                .toList();
    }

    private static List<?> getPrimitiveObjectList(
            List<Map<String, Object>> records,
            String idColumnName,
            Map<String, Object> record,
            String columnName
    ) {
        Object parentId = record.get(idColumnName);

        return records.stream()
                .filter(el -> parentId.equals(el.get(idColumnName)))
                .map(el -> el.get(columnName))
                .toList();
    }

    @SuppressWarnings("unchecked")
    private static <T extends Annotation> Optional<T> findAnnotationType(List<Annotation> annotations, Class<T> aClazz) {
        return (Optional<T>) annotations.stream()
                .filter(annotation -> annotation.annotationType() == aClazz)
                .findFirst();
    }
}
