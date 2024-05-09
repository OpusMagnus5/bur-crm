DROP PROCEDURE IF EXISTS operator_create_new;
/*PROCEDURE operator_create_new*/
CREATE OR REPLACE PROCEDURE operator_create_new(
    IN _opr_uuid operator.opr_uuid%TYPE,
    IN _opr_name operator.opr_name%TYPE,
    IN _opr_notes operator.opr_notes%TYPE,
    IN _opr_created_by operator.opr_created_by%TYPE
)
    LANGUAGE plpgsql
AS $$
BEGIN

    INSERT INTO operator(opr_uuid, opr_name, opr_notes, opr_created_by)
    VALUES(_opr_uuid, _opr_name, _opr_notes, _opr_created_by);

END$$;

DROP PROCEDURE IF EXISTS operator_get_by_name;
/*PROCEDURE operator_get_by_name*/
CREATE OR REPLACE PROCEDURE operator_get_by_name(
    IN _opr_name operator.opr_name%TYPE,
    OUT _cursor REFCURSOR
)
    LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT opr_id, opr_name
        FROM operator WHERE lower(opr_name) = lower(_opr_name);

END$$;

DROP PROCEDURE IF EXISTS operator_get_page;
/*PROCEDURE operator_get_page*/
CREATE OR REPLACE PROCEDURE operator_get_page(
    IN _offset NUMERIC,
    IN _max NUMERIC,
    IN _opr_name operator.opr_name%TYPE,
    OUT _cursor REFCURSOR,
    OUT _total_operators BIGINT
)
    LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT opr_id, opr_name
        FROM operator
        WHERE to_tsvector('simple', opr_name) @@ (phraseto_tsquery('simple', COALESCE(_opr_name, opr_name))::text || ':*')::tsquery
        ORDER BY opr_name
        OFFSET _offset
        LIMIT _max;

    SELECT count(opr_id)
    INTO _total_operators
    FROM operator
    WHERE to_tsvector('simple', opr_name) @@ (phraseto_tsquery('simple', COALESCE(_opr_name, opr_name))::text || ':*')::tsquery;

END$$;

DROP PROCEDURE IF EXISTS operator_delete;
/*PROCEDURE operator_delete*/
CREATE OR REPLACE PROCEDURE operator_delete(
    IN _opr_id operator.opr_id%TYPE
)
    LANGUAGE plpgsql
AS $$
BEGIN

    DELETE FROM operator WHERE opr_id = _opr_id;

END$$;

DROP PROCEDURE IF EXISTS operator_get_details;
/*PROCEDURE operator_get_details*/
CREATE OR REPLACE PROCEDURE operator_get_details(
    IN _opr_id operator.opr_id%TYPE,
    OUT _cursor REFCURSOR
)
    LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT opr_id, opr_version, opr_name, opr_notes, opr_created_at, opr_modified_at,
               c.usr_first_name as creator_usr_first_name, c.usr_last_name as creator_usr_last_name,
               m.usr_first_name as modifier_usr_first_name, m.usr_last_name as modifier_usr_last_name,
               jsonb_agg(prg.prg_name) as programs
        FROM operator opr
                 LEFT JOIN users c ON opr.opr_created_by = c.usr_id
                 LEFT JOIN users m ON opr.opr_modified_by = m.usr_id
                 LEFT JOIN program prg on opr.opr_id = prg.prg_operator_id
        WHERE opr_id = _opr_id
        GROUP BY opr_id;

END$$;

DROP PROCEDURE IF EXISTS operator_update;
/*PROCEDURE operator_update*/
CREATE OR REPLACE PROCEDURE operator_update(
    IN _opr_id operator.opr_id%TYPE,
    IN _opr_version operator.opr_version%TYPE,
    IN _opr_name operator.opr_name%TYPE,
    IN _opr_notes operator.opr_notes%TYPE,
    in _opr_modified_by operator.opr_modified_by%TYPE
)
    LANGUAGE plpgsql
AS $$
DECLARE
    _current_version INTEGER;
BEGIN

    SELECT opr_version
    INTO _current_version
    FROM operator
    WHERE opr_id = _opr_id;

    IF _current_version <> _opr_version THEN
        RAISE SQLSTATE '55000' USING MESSAGE = 'The resource with ID: ' || _opr_id || ' was changed by another user',
            TABLE = 'operator',
            COLUMN = 'opr_version',
            DETAIL = 'Provided version: ' || _opr_version || ', current version: ' || _current_version;
    END IF;

    UPDATE operator
    SET opr_version = opr_version + 1,
        opr_name = _opr_name,
        opr_notes = _opr_notes,
        opr_modified_by = _opr_modified_by,
        opr_modified_at = current_timestamp
    WHERE opr_id = _opr_id;

END$$;