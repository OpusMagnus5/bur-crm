DROP PROCEDURE IF EXISTS util_get_custom_types_attributes;
/*PROCEDURE util_get_custom_types_attributes*/
CREATE OR REPLACE PROCEDURE util_get_custom_types_attributes(
    IN _custom_types VARCHAR[],
    OUT _cursor REFCURSOR
)
LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT class.relname as name, attribute.attname as attribute_name
        FROM pg_class class
        LEFT JOIN pg_attribute attribute ON class.oid = attribute.attrelid
        WHERE relname = ANY (_custom_types)
        ORDER BY class.relname, attribute.attnum;

END$$;