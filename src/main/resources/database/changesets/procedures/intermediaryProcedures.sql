DROP PROCEDURE IF EXISTS intermediary_create_new;
/*PROCEDURE intermediary_create_new*/
CREATE OR REPLACE PROCEDURE intermediary_create_new(
    IN _itr_uuid intermediary.itr_uuid%TYPE,
    IN _itr_name intermediary.itr_name%TYPE,
    IN _itr_nip intermediary.itr_nip%TYPE,
    IN _itr_created_by intermediary.itr_created_by%TYPE
)
LANGUAGE plpgsql
AS $$
BEGIN

    INSERT INTO intermediary(itr_uuid, itr_name, itr_nip, itr_created_by)
    VALUES(_itr_uuid, _itr_name, _itr_nip, _itr_created_by);

END$$;

DROP PROCEDURE IF EXISTS intermediary_get_by_nip;
/*PROCEDURE intermediary_get_by_nip*/
CREATE OR REPLACE PROCEDURE intermediary_get_by_nip(
    IN _itr_nip intermediary.itr_nip%TYPE,
    OUT _cursor REFCURSOR
)
    LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT itr_id, itr_version, itr_name, itr_nip
        FROM intermediary WHERE itr_nip = _itr_nip;

END$$;

DROP PROCEDURE IF EXISTS intermediary_get_page;
/*PROCEDURE intermediary_get_page*/
CREATE OR REPLACE PROCEDURE intermediary_get_page(
    IN _offset NUMERIC,
    IN _max NUMERIC,
    IN _itr_name intermediary.itr_name%TYPE,
    IN _itr_nip intermediary.itr_nip%TYPE,
    OUT _cursor REFCURSOR,
    OUT _total_intermediaries BIGINT
)
LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT itr_id, itr_name, itr_nip
        FROM intermediary
        WHERE to_tsvector('simple', itr_name) @@ (phraseto_tsquery('simple', COALESCE(_itr_name, itr_name))::text || ':*')::tsquery
            AND itr_nip::varchar like '%' || COALESCE(_itr_nip, itr_nip)::varchar || '%'
        ORDER BY itr_name
        OFFSET _offset
        LIMIT _max;

    SELECT count(itr_id)
    INTO _total_intermediaries
    FROM intermediary
    WHERE to_tsvector('simple', itr_name) @@ (phraseto_tsquery('simple', COALESCE(_itr_name, itr_name))::text || ':*')::tsquery
        AND itr_nip::varchar like '%' || COALESCE(_itr_nip, itr_nip)::varchar || '%';

END$$;

DROP PROCEDURE IF EXISTS intermediary_delete;
/*PROCEDURE intermediary_delete*/
CREATE OR REPLACE PROCEDURE intermediary_delete(
    IN _itr_id intermediary.itr_id%TYPE
)
LANGUAGE plpgsql
AS $$
BEGIN

    DELETE FROM intermediary WHERE itr_id = _itr_id;

END$$;

DROP PROCEDURE IF EXISTS intermediary_get_details;
/*PROCEDURE intermediary_get_details*/
CREATE OR REPLACE PROCEDURE intermediary_get_details(
    IN _itr_id intermediary.itr_id%TYPE,
    OUT _cursor REFCURSOR
)
LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT itr_id, itr_version, itr_name, itr_nip, itr_created_at, itr_modified_at,
               c.usr_first_name as creator_usr_first_name, c.usr_last_name as creator_usr_last_name,
               m.usr_first_name as modifier_usr_first_name, m.usr_last_name as modifier_usr_last_name
        FROM intermediary itr
                 LEFT JOIN users c ON itr.itr_created_by = c.usr_id
                 LEFT JOIN users m ON itr.itr_modified_by = m.usr_id
        WHERE itr_id = _itr_id;

END$$;

DROP PROCEDURE IF EXISTS intermediary_update;
/*PROCEDURE intermediary_update*/
CREATE OR REPLACE PROCEDURE intermediary_update(
    IN _itr_id intermediary.itr_id%TYPE,
    IN _itr_version intermediary.itr_version%TYPE,
    IN _itr_name intermediary.itr_name%TYPE,
    IN _itr_nip intermediary.itr_nip%TYPE,
    in _itr_modified_by intermediary.itr_modified_by%TYPE
)
LANGUAGE plpgsql
AS $$
DECLARE
    _current_version INTEGER;
BEGIN

    SELECT itr_version
    INTO _current_version
    FROM intermediary
    WHERE itr_id = _itr_id;

    IF _current_version <> _itr_version THEN
        RAISE SQLSTATE '55000' USING MESSAGE = 'The resource with ID: ' || _itr_id || ' was changed by another user',
            TABLE = 'intermediary',
            COLUMN = 'itr_version',
            DETAIL = 'Provided version: ' || _itr_version || ', current version: ' || _current_version;
    END IF;

    UPDATE intermediary
    SET itr_version = itr_version + 1,
        itr_name = _itr_name,
        itr_nip = _itr_nip,
        itr_modified_by = _itr_modified_by,
        itr_modified_at = current_timestamp
    WHERE itr_id = _itr_id;

END$$;

DROP PROCEDURE IF EXISTS intermediary_get_all;
/*PROCEDURE intermediary_get_all*/
CREATE OR REPLACE PROCEDURE intermediary_get_all(
    OUT _cursor REFCURSOR
)
LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT itr_id, itr_version, itr_name, itr_nip
        FROM intermediary;

END$$;