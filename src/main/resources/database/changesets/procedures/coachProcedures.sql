DROP PROCEDURE IF EXISTS coach_create_new;
/*PROCEDURE coach_create_new*/
CREATE OR REPLACE PROCEDURE coach_create_new(
    IN _coa_uuid coach.coa_uuid%TYPE,
    IN _coa_first_name coach.coa_first_name%TYPE,
    IN _coa_last_name coach.coa_last_name%TYPE,
    IN _coa_pesel coach.coa_pesel%TYPE,
    IN _coa_created_by coach.coa_created_by%TYPE
)
LANGUAGE plpgsql
AS $$
BEGIN

    INSERT INTO coach(coa_uuid, coa_first_name, coa_last_name, coa_pesel, coa_created_by)
    VALUES(_coa_uuid, _coa_first_name, _coa_last_name, _coa_pesel, _coa_created_by);

END$$;

DROP PROCEDURE IF EXISTS coach_get_by_nip;
/*PROCEDURE coach_get_by_nip*/
CREATE OR REPLACE PROCEDURE coach_get_by_nip(
    IN _coa_pesel coach.coa_pesel%TYPE,
    OUT _cursor REFCURSOR
)
    LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT coa_id, coa_version, coa_first_name, coa_last_name, coa_pesel
        FROM coach
        WHERE coa_pesel = _coa_pesel;

END$$;