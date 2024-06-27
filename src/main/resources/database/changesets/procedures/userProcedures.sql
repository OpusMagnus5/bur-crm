DROP PROCEDURE IF EXISTS users_create_new_or_update;
/*PROCEDURE users_create_new_or_update*/
CREATE OR REPLACE PROCEDURE users_create_new_or_update(
    IN _usr_id users.usr_id%TYPE,
    IN _usr_version users.usr_version%TYPE,
    IN _usr_uuid users.usr_uuid%TYPE,
    IN _usr_password users.usr_password%TYPE,
    IN _usr_email users.usr_email%TYPE,
    IN _usr_first_name users.usr_first_name%TYPE,
    IN _usr_last_name users.usr_last_name%TYPE,
    IN _usr_roles users.usr_roles%TYPE,
    IN _usr_created_by users.usr_created_by%TYPE
)
LANGUAGE plpgsql
AS $$
DECLARE
    _current_version INTEGER;
BEGIN

    IF _usr_id IS NULL THEN
        INSERT INTO users (usr_uuid, usr_password, usr_email, usr_first_name, usr_last_name, usr_roles, usr_last_login,
                           usr_modified_at, usr_created_by, usr_modified_by)
        VALUES (_usr_uuid, _usr_password, _usr_email, _usr_first_name, _usr_last_name,
                _usr_roles, null, null, _usr_created_by, null);

    ELSE

        SELECT usr_version
        INTO _current_version
        FROM users
        WHERE usr_id = _usr_id;

        IF _current_version <> _usr_version THEN
            RAISE SQLSTATE '55000' USING MESSAGE = 'The resource with ID: ' || _usr_id || ' was changed by another user',
                TABLE = 'users',
                COLUMN = 'usr_version',
                DETAIL = 'Provided version: ' || _usr_version || ', current version: ' || _current_version;
        END IF;

        UPDATE users
        SET usr_version = usr_version + 1,
            usr_email = _usr_email,
            usr_first_name = _usr_first_name,
            usr_last_name = _usr_last_name,
            usr_roles = _usr_roles,
            usr_modified_by = _usr_created_by,
            usr_modified_at = current_timestamp
        WHERE usr_id = _usr_id;

    END IF;


END$$;

DROP PROCEDURE IF EXISTS users_delete;
/*PROCEDURE users_delete*/
CREATE OR REPLACE PROCEDURE users_delete(
    IN _usr_id users.usr_id%TYPE
)
LANGUAGE plpgsql
AS $$
BEGIN

    DELETE FROM users WHERE usr_id = _usr_id;

END$$;

DROP PROCEDURE IF EXISTS users_get_by_email;
/*PROCEDURE users_get_by_email*/
CREATE OR REPLACE PROCEDURE users_get_by_email(
    IN _usr_email users.usr_email%TYPE,
    OUT _cursor REFCURSOR
)
LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
    SELECT usr_id, usr_uuid, usr_version, usr_password, usr_email, usr_first_name, usr_last_name, usr_roles, usr_last_login,
           usr_created_at, usr_modified_at, usr_created_by, usr_modified_by
    FROM users WHERE usr_email = _usr_email;

END$$;

DROP PROCEDURE IF EXISTS users_get_details;
/*PROCEDURE users_get_details*/
CREATE OR REPLACE PROCEDURE users_get_details(
    IN _usr_id users.usr_id%TYPE,
    OUT _cursor REFCURSOR

)
LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT u.usr_id as usr_id, u.usr_uuid as usr_uuid, u.usr_version as usr_version, u.usr_password as usr_password,
               u.usr_email as usr_email, u.usr_first_name as usr_first_name, u.usr_last_name as usr_last_name, u.usr_roles as usr_roles,
               u.usr_last_login as usr_last_login, u.usr_created_at as usr_created_at, u.usr_modified_at as usr_modified_at,
               u.usr_created_by as usr_created_by, u.usr_modified_by as usr_modified_by,
               c.usr_first_name as creator_usr_first_name, c.usr_last_name as creator_usr_last_name,
               m.usr_first_name as modifier_usr_first_name, m.usr_last_name as modifier_usr_last_name
        FROM users u
        LEFT JOIN users c ON u.usr_created_by = c.usr_id
        LEFT JOIN users m ON u.usr_modified_by = m.usr_id
        WHERE u.usr_id = _usr_id;

END$$;

DROP PROCEDURE IF EXISTS users_get_page_of_users;
/*PROCEDURE users_get_page_of_users*/
CREATE OR REPLACE PROCEDURE users_get_page_of_users(
    IN _usr_id_excluded users.usr_id%TYPE,
    IN _offset NUMERIC,
    IN _max NUMERIC,
    OUT _cursor REFCURSOR,
    OUT _total_users BIGINT
)
LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT usr_id, usr_uuid, usr_version, usr_password, usr_email, usr_first_name, usr_last_name, usr_roles, usr_last_login,
               usr_created_at, usr_modified_at, usr_created_by, usr_modified_by
        FROM users
        WHERE usr_id != _usr_id_excluded
        ORDER BY usr_last_name, usr_first_name
        OFFSET _offset
        LIMIT _max;

    SELECT count(usr_id)
    INTO _total_users
    FROM users;

END$$;

DROP PROCEDURE IF EXISTS users_change_password;
/*PROCEDURE users_change_password*/
CREATE OR REPLACE PROCEDURE users_change_password(
    IN _usr_id users.usr_id%TYPE,
    IN _usr_version users.usr_version%TYPE,
    IN _usr_password users.usr_password%TYPE,
    IN _usr_modified_by users.usr_id%TYPE
)
LANGUAGE plpgsql
AS $$
DECLARE
    _current_version INTEGER;
BEGIN

    SELECT usr_version
    INTO _current_version
    FROM users
    WHERE usr_id = _usr_id;

    IF _current_version <> _usr_version THEN
        RAISE SQLSTATE '55000' USING MESSAGE = 'The resource with ID: ' || _usr_id || ' was changed by another user',
            TABLE = 'users',
            COLUMN = 'usr_version',
            DETAIL = 'Provided version: ' || _usr_version || ', current version: ' || _current_version;
    END IF;

    UPDATE users
    SET usr_version = usr_version + 1,
        usr_password = _usr_password,
        usr_modified_at = current_timestamp,
        usr_modified_by = _usr_modified_by
    WHERE usr_id = _usr_id;

END$$;

DROP PROCEDURE IF EXISTS users_set_last_login;
/*PROCEDURE users_set_last_login*/
CREATE OR REPLACE PROCEDURE users_set_last_login(
    IN _usr_id users.usr_id%TYPE
)
LANGUAGE plpgsql
AS $$
BEGIN

    UPDATE users
    SET usr_last_login = current_timestamp
    WHERE usr_id = _usr_id;

END$$;

DROP PROCEDURE IF EXISTS users_create_system_users;
/*PROCEDURE users_create_system_users*/
CREATE OR REPLACE PROCEDURE users_create_system_users(
    IN _usr_id users.usr_id%TYPE,
    IN _usr_uuid users.usr_uuid%TYPE,
    IN _usr_password users.usr_password%TYPE,
    IN _usr_email users.usr_email%TYPE,
    IN _usr_first_name users.usr_first_name%TYPE,
    IN _usr_last_name users.usr_last_name%TYPE,
    IN _usr_roles users.usr_roles%TYPE,
    IN _usr_created_by users.usr_created_by%TYPE
)
    LANGUAGE plpgsql
AS $$
DECLARE
    _user_exists INTEGER;
BEGIN

    SELECT COUNT(*)
    INTO _user_exists
    FROM users
    WHERE usr_id = _usr_id;

    IF _user_exists = 0 THEN
        INSERT INTO users (usr_id, usr_uuid, usr_password, usr_email, usr_first_name, usr_last_name, usr_roles, usr_created_by)
        VALUES (_usr_id, _usr_uuid, _usr_password, _usr_email, _usr_first_name, _usr_last_name, _usr_roles, _usr_created_by);
    END IF;


END$$;