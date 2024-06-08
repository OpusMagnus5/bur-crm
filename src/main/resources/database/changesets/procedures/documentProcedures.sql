DROP PROCEDURE IF EXISTS document_add_new;
/*PROCEDURE document_add_new*/
CREATE OR REPLACE PROCEDURE document_add_new(
    IN _doc_uuid document.doc_uuid%TYPE,
    IN _doc_service_id document.doc_service_id%TYPE,
    IN _doc_coach_id document.doc_coach_id%TYPE,
    IN _doc_type document.doc_type%TYPE,
    IN _doc_file_extension document.doc_file_extension%TYPE,
    IN _doc_created_by document.doc_created_by%TYPE,
    OUT _doc_id document.doc_id%TYPE
)
LANGUAGE plpgsql
AS $$
BEGIN

    INSERT INTO document(doc_uuid, doc_service_id, doc_coach_id, doc_type, doc_file_extension, doc_created_by)
    VALUES(_doc_uuid, _doc_service_id, _doc_coach_id, _doc_type, _doc_file_extension, _doc_created_by)
    RETURNING doc_id INTO _doc_id;

END$$;

DROP PROCEDURE IF EXISTS document_get_all_for_service;
/*PROCEDURE document_get_all_for_service*/
CREATE OR REPLACE PROCEDURE document_get_all_for_service(
    IN _doc_service_id document.doc_service_id%TYPE,
    OUT _cursor REFCURSOR
)
LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT doc_id, doc_type, doc_coach_id,
               service.srv_id as service_srv_id, service.srv_type as service_srv_type
        FROM document
        LEFT JOIN service ON document.doc_service_id = service.srv_id
        WHERE doc_service_id = _doc_service_id;

END$$;