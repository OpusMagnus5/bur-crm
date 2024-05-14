DROP PROCEDURE IF EXISTS program_create_new;
/*PROCEDURE program_create_new*/
CREATE OR REPLACE PROCEDURE program_create_new(
    IN _prg_uuid program.prg_uuid%TYPE,
    IN _prg_name program.prg_name%TYPE,
    IN _prg_operator_id program.prg_operator_id%TYPE,
    IN _prg_created_by program.prg_created_by%TYPE
)
    LANGUAGE plpgsql
AS $$
BEGIN

    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by)
    VALUES(_prg_uuid, _prg_name, _prg_operator_id, _prg_created_by);

END$$;

DROP PROCEDURE IF EXISTS program_get_page;
/*PROCEDURE program_get_page*/
CREATE OR REPLACE PROCEDURE program_get_page(
    IN _offset NUMERIC,
    IN _max NUMERIC,
    IN _prg_or_opr_name program.prg_name%TYPE,
    OUT _cursor REFCURSOR,
    OUT _total_programs BIGINT
)
LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT prg_id, prg_name, opr.opr_name as operator_opr_name
        FROM program
        LEFT JOIN operator opr ON prg_operator_id = opr.opr_id
        WHERE to_tsvector('simple', prg_name) @@ (phraseto_tsquery('simple', COALESCE(_prg_or_opr_name, prg_name))::text || ':*')::tsquery
            OR to_tsvector('simple', opr.opr_name) @@ (phraseto_tsquery('simple', COALESCE(_prg_or_opr_name, opr.opr_name))::text || ':*')::tsquery
        ORDER BY prg_name
        OFFSET _offset
        LIMIT _max;

    SELECT count(prg_id)
    INTO _total_programs
    FROM program
    WHERE to_tsvector('simple', prg_name) @@ (phraseto_tsquery('simple', COALESCE(_prg_or_opr_name, prg_name))::text || ':*')::tsquery;

END$$;

DROP PROCEDURE IF EXISTS program_get_by_name;
/*PROCEDURE program_get_by_name*/
CREATE OR REPLACE PROCEDURE program_get_by_name(
    IN _prg_name program.prg_name%TYPE,
    OUT _cursor REFCURSOR
)
LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT prg_id, prg_name
        FROM program
        WHERE lower(prg_name) = lower(_prg_name);

END$$;

DROP PROCEDURE IF EXISTS program_delete;
/*PROCEDURE program_delete*/
CREATE OR REPLACE PROCEDURE program_delete(
    IN _prg_id program.prg_id%TYPE
)
LANGUAGE plpgsql
AS $$
BEGIN

    DELETE FROM program WHERE prg_id = _prg_id;

END$$;

DROP PROCEDURE IF EXISTS program_get_details;
/*PROCEDURE program_get_details*/
CREATE OR REPLACE PROCEDURE program_get_details(
    IN _prg_id program.prg_id%TYPE,
    OUT _cursor REFCURSOR
)
    LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT prg_id, prg_version, prg_name, prg_created_at, prg_modified_at,
               c.usr_first_name as creator_usr_first_name, c.usr_last_name as creator_usr_last_name,
               m.usr_first_name as modifier_usr_first_name, m.usr_last_name as modifier_usr_last_name,
               opr.opr_name as operator_opr_name, opr.opr_id as operator_opr_id
        FROM program prg
        LEFT JOIN users c ON prg.prg_created_by = c.usr_id
        LEFT JOIN users m ON prg.prg_modified_by = m.usr_id
        LEFT JOIN operator opr on prg.prg_operator_id = opr.opr_id
        WHERE prg_id = _prg_id;

END$$;

DROP PROCEDURE IF EXISTS program_update;
/*PROCEDURE program_update*/
CREATE OR REPLACE PROCEDURE program_update(
    IN _prg_id program.prg_id%TYPE,
    IN _prg_version program.prg_version%TYPE,
    IN _prg_name program.prg_name%TYPE,
    IN _prg_operator_id program.prg_operator_id%TYPE,
    in _prg_modified_by program.prg_modified_by%TYPE
)
LANGUAGE plpgsql
AS $$
DECLARE
    _current_version INTEGER;
BEGIN

    SELECT prg_version
    INTO _current_version
    FROM program
    WHERE prg_id = _prg_id;

    IF _current_version <> _prg_version THEN
        RAISE SQLSTATE '55000' USING MESSAGE = 'The resource with ID: ' || _prg_id || ' was changed by another user',
            TABLE = 'program',
            COLUMN = 'prg_version',
            DETAIL = 'Provided version: ' || _prg_version || ', current version: ' || _current_version;
    END IF;

    UPDATE program
    SET prg_version = prg_version + 1,
        prg_name = _prg_name,
        prg_operator_id = _prg_operator_id,
        prg_modified_by = _prg_modified_by,
        prg_modified_at = current_timestamp
    WHERE prg_id = _prg_id;

END$$;