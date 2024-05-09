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