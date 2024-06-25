DROP PROCEDURE IF EXISTS error_add_new;
/*PROCEDURE error_add_new*/
CREATE OR REPLACE PROCEDURE error_add_new(
    IN _err_uuid error.err_uuid%TYPE,
    IN _err_user_id error.err_user_id%TYPE,
    IN _err_class error.err_class%TYPE,
    IN _err_message error.err_message%TYPE,
    IN _err_stacktrace error.err_stacktrace%TYPE,
    IN _err_is_web error.err_is_web%TYPE
)
LANGUAGE plpgsql
AS $$
DECLARE
BEGIN

    INSERT INTO error (err_uuid, err_user_id, err_class, err_message, err_stacktrace, err_is_web)
    VALUES (_err_uuid, _err_user_id, _err_class, _err_message, _err_stacktrace, COALESCE(_err_is_web, false));

END$$;