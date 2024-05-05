DROP PROCEDURE IF EXISTS operator_create_new;
/*PROCEDURE operator_create_new*/
CREATE OR REPLACE PROCEDURE operator_create_new(
    IN _opr_uuid operator.opr_uuid%TYPE,
    IN _opr_name operator.opr_name%TYPE,
    IN _opr_phone_number operator.opr_phone_number%TYPE,
    IN _opr_created_by operator.opr_created_by%TYPE
)
    LANGUAGE plpgsql
AS $$
BEGIN

    INSERT INTO operator(opr_uuid, opr_version, opr_name, opr_phone_number, opr_created_at, opr_created_by)
    VALUES(_opr_uuid, 0, _opr_name, _opr_phone_number, current_timestamp, _opr_created_by);

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