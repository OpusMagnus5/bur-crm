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
        WITH _services AS (
            SELECT srv_id, srv_type, srv_number_of_participants, srv_uuid
            FROM service
            WHERE srv_id = _doc_service_id
        ),
        _filtered_documents AS (
            SELECT doc_id, doc_type, doc_coach_id, doc_service_id
            FROM document
            WHERE doc_service_id = _doc_service_id
              AND (doc_type IS NULL OR doc_type = _doc_type)
              AND (_doc_coach_id IS NULL OR doc_coach_id IS NULL OR doc_coach_id = _doc_coach_id)
        )
        SELECT COALESCE(docs.doc_id, row_number() over (ORDER BY _services.srv_id)) as doc_id,
               doc_type, doc_coach_id, _services.srv_id as service_srv_id, _services.srv_type as service_srv_type,
               _services.srv_number_of_participants as service_srv_number_of_participants,
               _services.srv_uuid as service_srv_uuid
        FROM _services
        LEFT JOIN _filtered_documents docs ON _services.srv_id = docs.doc_service_id;

END$$;

DROP PROCEDURE IF EXISTS document_get_by_ids;
/*PROCEDURE document_get_by_ids*/
CREATE OR REPLACE PROCEDURE document_get_by_ids(
    IN _doc_ids BIGINT[],
    OUT _cursor REFCURSOR
)
LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT doc_id, doc_service_id, doc_file_extension, doc_file_name, doc_uuid, doc_type,
               service.srv_id as service_srv_id, service.srv_number as service_srv_number, service.srv_uuid as service_srv_uuid
        FROM document
        LEFT JOIN service ON document.doc_service_id = service.srv_id
        WHERE doc_id = ANY(_doc_ids);

END$$;

DROP PROCEDURE IF EXISTS document_delete_documents;
/*PROCEDURE document_delete_documents*/
CREATE OR REPLACE PROCEDURE document_delete_documents(
    IN _doc_ids BIGINT[]
)
LANGUAGE plpgsql
AS $$
DECLARE
    _doc_id BIGINT;
BEGIN

    FOREACH _doc_id IN ARRAY _doc_ids LOOP
        DELETE FROM document WHERE doc_id = _doc_id;
    END LOOP;

END$$;