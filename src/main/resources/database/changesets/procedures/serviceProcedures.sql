DROP PROCEDURE IF EXISTS service_create_new;
/*PROCEDURE service_create_new*/
CREATE OR REPLACE PROCEDURE service_create_new(
    IN _srv_uuid service.srv_uuid%TYPE,
    IN _srv_bur_card_id service.srv_bur_card_id%TYPE,
    IN _srv_number service.srv_number%TYPE,
    IN _srv_name service.srv_name%TYPE,
    IN _srv_type service.srv_type%TYPE,
    IN _srv_start_date service.srv_start_date%TYPE,
    IN _srv_end_date service.srv_end_date%TYPE,
    IN _srv_number_of_participants service.srv_number_of_participants%TYPE,
    IN _srv_service_provider_id service.srv_service_provider_id%TYPE,
    IN _srv_program_id service.srv_program_id%TYPE,
    IN _srv_customer_id service.srv_customer_id%TYPE,
    IN _srv_intermediary_id service.srv_intermediary_id%TYPE,
    IN _srv_created_by service.srv_created_by%TYPE,
    IN _srv_coach_ids BIGINT[]
)
LANGUAGE plpgsql
AS $$
DECLARE
    _created_service_id BIGINT;
    _coach_id coach.coa_id%TYPE;
BEGIN

    INSERT INTO service(srv_uuid, srv_bur_card_id, srv_number, srv_name, srv_type, srv_start_date, srv_end_date,
                        srv_number_of_participants, srv_service_provider_id, srv_program_id, srv_customer_id,
                        srv_intermediary_id, srv_created_by)
    VALUES(_srv_uuid, _srv_bur_card_id, _srv_number, _srv_name, _srv_type, _srv_start_date, _srv_end_date,
           _srv_number_of_participants, _srv_service_provider_id, _srv_program_id, _srv_customer_id,
           _srv_intermediary_id, _srv_created_by)
    RETURNING srv_id
    INTO _created_service_id;

    FOREACH _coach_id IN ARRAY _srv_coach_ids LOOP
        INSERT INTO service_coach(service_id, coach_id)
        VALUES(_created_service_id, _coach_id);
    END LOOP;

END$$;

DROP PROCEDURE IF EXISTS service_get_page;
/*PROCEDURE service_get_page*/
CREATE OR REPLACE PROCEDURE service_get_page(
    IN _offset NUMERIC,
    IN _max NUMERIC,
    OUT _cursor REFCURSOR,
    OUT _total_services BIGINT
)
    LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT srv_id, srv_name, srv_number, srv_start_date, srv_end_date, srv_type,
               opr.opr_name as operator_opr_name,
               cst.cst_name as customer_cst_name,
               spr.spr_name as service_provider_spr_name
        FROM service
        LEFT JOIN program prg ON srv_program_id = prg.prg_id
        LEFT JOIN operator opr on opr.opr_id = prg.prg_operator_id
        LEFT JOIN customer cst ON srv_customer_id = cst.cst_id
        LEFT JOIN service_provider spr ON srv_service_provider_id = spr.spr_id
        ORDER BY srv_start_date, srv_name
        OFFSET _offset
        LIMIT _max;

    SELECT count(srv_id)
    INTO _total_services
    FROM service;

END$$;