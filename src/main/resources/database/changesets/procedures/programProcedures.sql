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
    IN _prg_name program.prg_name%TYPE,
    OUT _cursor REFCURSOR,
    OUT _total_programs BIGINT
)
LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT prg_id, prg_name
        FROM program
        WHERE to_tsvector('simple', prg_name) @@ (phraseto_tsquery('simple', COALESCE(_prg_name, prg_name))::text || ':*')::tsquery
        ORDER BY prg_name
        OFFSET _offset
        LIMIT _max;

    SELECT count(prg_id)
    INTO _total_programs
    FROM program
    WHERE to_tsvector('simple', prg_name) @@ (phraseto_tsquery('simple', COALESCE(_prg_name, prg_name))::text || ':*')::tsquery;

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