DROP PROCEDURE IF EXISTS document_add_new;
/*PROCEDURE document_add_new*/
CREATE OR REPLACE PROCEDURE document_add_new(
    IN _documents document[]
)
LANGUAGE plpgsql
AS $$
DECLARE
    _document document;
BEGIN

    FOREACH _document IN ARRAY _documents LOOP
        INSERT INTO document(doc_uuid, doc_service_id, doc_coach_id, doc_type, doc_file_name, doc_file_extension, doc_created_by)
        VALUES(_document.doc_uuid, _document.doc_service_id, _document.doc_coach_id, _document.doc_type, _document.doc_file_name,
               _document.doc_file_extension, _document.doc_created_by);
    END LOOP;

END$$;

DROP PROCEDURE IF EXISTS document_get_all_for_service;
/*PROCEDURE document_get_all_for_service*/
CREATE OR REPLACE PROCEDURE document_get_all_for_service(
    IN _doc_service_id document.doc_service_id%TYPE,
    IN _doc_type document.doc_type%TYPE,
    IN _doc_coach_id document.doc_coach_id%TYPE,
    OUT _cursor REFCURSOR
)
LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        WITH Documents AS (
            SELECT ROW_NUMBER() OVER (ORDER BY service.srv_id) AS row_num, doc_id, doc_type, doc_coach_id,
                   service.srv_id as service_srv_id, service.srv_type as service_srv_type,
                   service.srv_number_of_participants as service_srv_number_of_participants,
                   service.srv_uuid as service_srv_uuid
            FROM document
            RIGHT JOIN service ON doc_service_id = service.srv_id
            WHERE service.srv_id = _doc_service_id
              AND (doc_type IS NULL OR doc_type = _doc_type)
              AND (_doc_coach_id IS NULL OR doc_coach_id IS NULL OR doc_coach_id = _doc_coach_id)
        )
        SELECT COALESCE(doc_id, row_num) as doc_id, doc_type, doc_coach_id, service_srv_id, service_srv_type,
               service_srv_number_of_participants, service_srv_uuid
        FROM Documents;



END$$;