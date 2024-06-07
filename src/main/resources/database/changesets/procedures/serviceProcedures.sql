DROP PROCEDURE IF EXISTS service_create_or_update;
/*PROCEDURE service_create_or_update*/
CREATE OR REPLACE PROCEDURE service_create_or_update(
    IN _srv_id service.srv_id%TYPE,
    IN _srv_version service.srv_version%TYPE,
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
    _current_version INTEGER;
BEGIN

    IF _srv_id IS NULL THEN
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

    ELSE

        SELECT srv_version
        INTO _current_version
        FROM service
        WHERE srv_id = _srv_id;

        IF _current_version <> _srv_version THEN
            RAISE SQLSTATE '55000' USING MESSAGE = 'The resource with ID: ' || _srv_id || ' was changed by another user',
                TABLE = 'service',
                COLUMN = 'srv_version',
                DETAIL = 'Provided version: ' || _srv_version || ', current version: ' || _current_version;
        END IF;

        DELETE FROM service_coach WHERE service_id = _srv_id;

        UPDATE service
        SET srv_version = srv_version + 1,
            srv_bur_card_id = _srv_bur_card_id,
            srv_number = _srv_number,
            srv_name = _srv_name,
            srv_type = _srv_type,
            srv_start_date = _srv_start_date,
            srv_end_date = _srv_end_date,
            srv_number_of_participants = _srv_number_of_participants,
            srv_service_provider_id = _srv_service_provider_id,
            srv_program_id = _srv_program_id,
            srv_customer_id = _srv_customer_id,
            srv_intermediary_id = _srv_intermediary_id,
            srv_modified_by = _srv_created_by,
            srv_modified_at = current_timestamp
        WHERE srv_id = _srv_id;

        FOREACH _coach_id IN ARRAY _srv_coach_ids LOOP
                INSERT INTO service_coach(service_id, coach_id)
                VALUES(_srv_id, _coach_id);
        END LOOP;

    END IF;



END$$;

DROP PROCEDURE IF EXISTS service_get_page;
/*PROCEDURE service_get_page*/
CREATE OR REPLACE PROCEDURE service_get_page(
    IN _offset NUMERIC,
    IN _max NUMERIC,
    IN _srv_number service.srv_number%TYPE,
    IN _srv_type service.srv_type%TYPE,
    IN _srv_service_provider_id service.srv_service_provider_id%TYPE,
    IN _srv_customer_id service.srv_customer_id%TYPE,
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
        WHERE srv_number LIKE '%' || COALESCE(_srv_number, srv_number) || '%'
            AND srv_type = COALESCE(_srv_type, srv_type)
            AND srv_service_provider_id = COALESCE(_srv_service_provider_id, srv_service_provider_id)
            AND srv_customer_id = COALESCE(_srv_customer_id, srv_customer_id)
        ORDER BY srv_start_date, srv_name
        OFFSET _offset
        LIMIT _max;

    SELECT count(srv_id)
    INTO _total_services
    FROM service;

END$$;

DROP PROCEDURE IF EXISTS service_get_details;
/*PROCEDURE program_get_details*/
CREATE OR REPLACE PROCEDURE service_get_details(
    IN _srv_id service.srv_id%TYPE,
    OUT _cursor REFCURSOR
)
    LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT srv_id, srv_uuid, srv_bur_card_id, srv_number, srv_name, srv_type, srv_start_date, srv_end_date,
               srv_number_of_participants, srv_service_provider_id, srv_customer_id, srv_program_id, srv_intermediary_id,
               coaches.coach_id as srv_coach_ids,
               creator.usr_first_name as creator_usr_first_name, creator.usr_last_name as creator_usr_last_name,
               creator.usr_id as creator_usr_id,
               modifier.usr_first_name as modifier_usr_first_name, modifier.usr_last_name as modifier_usr_last_name,
               modifier.usr_id as modifier_usr_id,
               program.prg_name as program_prg_name, program.prg_id as program_prg_id,
               operator.opr_name as operator_opr_name, operator.opr_id as operator_opr_id,
               customer.cst_name as customer_cst_name, customer.cst_id as customer_cst_id ,
               service_provider.spr_name as service_provider_spr_name, service_provider.spr_id as service_provider_spr_id,
               coach.coa_first_name as coach_coa_first_name, coach.coa_last_name as coach_coa_last_name, coach.coa_id as coach_coa_id,
               intermediary.itr_id as intermediary_itr_id, intermediary.itr_name as intermediary_itr_name
        FROM service
        LEFT JOIN service_coach coaches ON coaches.service_id = service.srv_id
        LEFT JOIN users creator ON creator.usr_id = srv_created_by
        LEFT JOIN users modifier ON modifier.usr_id = service.srv_modified_by
        LEFT JOIN program ON program.prg_id = service.srv_program_id
        LEFT JOIN operator ON program.prg_operator_id = operator.opr_id
        LEFT JOIN customer ON customer.cst_id = service.srv_customer_id
        LEFT JOIN service_provider ON service_provider.spr_id = service.srv_service_provider_id
        LEFT JOIN coach ON coach.coa_id = coaches.coach_id
        LEFT JOIN intermediary ON intermediary.itr_id = service.srv_intermediary_id
        WHERE srv_id = _srv_id;

END$$;

DROP PROCEDURE IF EXISTS service_get_to_status_check;
/*PROCEDURE service_get_to_status_check*/
CREATE OR REPLACE PROCEDURE service_get_to_status_check(
    OUT _cursor REFCURSOR
)
LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT srv_id, srv_bur_card_id
        FROM service
        WHERE srv_status = 'PUBLISHED'
        AND srv_end_date <= current_date
        AND srv_bur_card_id IS NOT NULL
        FOR UPDATE;

END$$;

DROP PROCEDURE IF EXISTS service_update_status;
DROP TYPE IF EXISTS service_status_data;
CREATE TYPE service_status_data AS (_srv_id BIGINT, _srv_status VARCHAR);
/*PROCEDURE service_update_status*/
CREATE OR REPLACE PROCEDURE service_update_status(
    IN _service_status_data service_status_data[]
)
LANGUAGE plpgsql
AS $$
DECLARE
    _status_data service_status_data;
BEGIN

    FOREACH _status_data IN ARRAY _service_status_data LOOP
        UPDATE service
        SET srv_status = _status_data._srv_status,
            srv_version = srv_version + 1,
            srv_modified_at = current_timestamp
        WHERE srv_id = _status_data._srv_id;
    END LOOP;

END$$;