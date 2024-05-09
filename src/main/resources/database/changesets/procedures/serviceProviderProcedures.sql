DROP PROCEDURE IF EXISTS service_provider_create_new;
/*PROCEDURE service_provider_create_new*/
CREATE OR REPLACE PROCEDURE service_provider_create_new(
    IN _spr_uuid service_provider.spr_uuid%TYPE,
    IN _spr_bur_id service_provider.spr_bur_id%TYPE,
    IN _spr_name service_provider.spr_name%TYPE,
    IN _spr_nip service_provider.spr_nip%TYPE,
    IN _spr_created_by service_provider.spr_created_by%TYPE
)
LANGUAGE plpgsql
AS $$
BEGIN

    INSERT INTO service_provider(spr_uuid, spr_bur_id, spr_name, spr_nip, spr_created_by)
    VALUES(_spr_uuid, _spr_bur_id, _spr_name, _spr_nip, _spr_created_by);

END$$;

DROP PROCEDURE IF EXISTS service_provider_delete;
/*PROCEDURE service_provider_delete*/
CREATE OR REPLACE PROCEDURE service_provider_delete(
    IN _spr_id service_provider.spr_id%TYPE
)
    LANGUAGE plpgsql
AS $$
BEGIN

    DELETE FROM service_provider WHERE spr_id = _spr_id;

END$$;


DROP PROCEDURE IF EXISTS service_provider_get_by_nip;
/*PROCEDURE service_provider_get_by_id*/
CREATE OR REPLACE PROCEDURE service_provider_get_by_nip(
    IN _spr_nip service_provider.spr_nip%TYPE,
    OUT _cursor REFCURSOR
)
    LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT spr_id, spr_uuid, spr_bur_id, spr_version, spr_name, spr_nip
        FROM service_provider WHERE spr_nip = _spr_nip;

END$$;

DROP PROCEDURE IF EXISTS service_provider_get_page;
/*PROCEDURE users_get_page_of_users*/
CREATE OR REPLACE PROCEDURE service_provider_get_page(
    IN _offset NUMERIC,
    IN _max NUMERIC,
    OUT _cursor REFCURSOR,
    OUT _total_providers BIGINT
)
    LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT spr_id, spr_uuid, spr_bur_id, spr_version, spr_name, spr_nip
        FROM service_provider
        ORDER BY spr_name
        OFFSET _offset
        LIMIT _max;

    SELECT count(spr_id)
    INTO _total_providers
    FROM service_provider;

END$$;

DROP PROCEDURE IF EXISTS service_provider_update;
/*PROCEDURE service_provider_update*/
CREATE OR REPLACE PROCEDURE service_provider_update(
    IN _spr_id service_provider.spr_id%TYPE,
    IN _spr_version service_provider.spr_version%TYPE,
    IN _spr_name service_provider.spr_name%TYPE,
    IN _spr_nip service_provider.spr_nip%TYPE,
    in _spr_modified_by service_provider.spr_modified_by%TYPE
)
    LANGUAGE plpgsql
AS $$
DECLARE
    _current_version INTEGER;
BEGIN

    SELECT spr_version
    INTO _current_version
    FROM service_provider
    WHERE spr_id = _spr_id;

    IF _current_version <> _spr_version THEN
        RAISE SQLSTATE '55000' USING MESSAGE = 'The resource with ID: ' || _spr_id || ' was changed by another user',
        TABLE = 'service_provider',
        COLUMN = 'spr_version',
        DETAIL = 'Provided version: ' || _spr_version || ', current version: ' || _current_version;
    END IF;

    UPDATE service_provider
    SET spr_version = spr_version + 1,
        spr_name = _spr_name,
        spr_nip = _spr_nip,
        spr_modified_by = _spr_modified_by,
        spr_modified_at = current_timestamp
    WHERE spr_id = _spr_id;

END$$;

DROP PROCEDURE IF EXISTS service_provider_get_details;
/*PROCEDURE service_provider_get_details*/
CREATE OR REPLACE PROCEDURE service_provider_get_details(
    IN _spr_id service_provider.spr_id%TYPE,
    OUT _cursor REFCURSOR
)
    LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT spr_id, spr_uuid, spr_bur_id, spr_version, spr_name, spr_nip, spr_created_at, spr_modified_at,
               c.usr_first_name as creator_usr_first_name, c.usr_last_name as creator_usr_last_name,
               m.usr_first_name as modifier_usr_first_name, m.usr_last_name as modifier_usr_last_name
        FROM service_provider spr
        LEFT JOIN users c ON spr.spr_created_by = c.usr_id
        LEFT JOIN users m ON spr.spr_modified_by = m.usr_id
        WHERE spr_id = _spr_id;

END$$;