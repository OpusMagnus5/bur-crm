CREATE TABLE document(
    doc_id BIGSERIAL PRIMARY KEY,
    doc_uuid UUID UNIQUE NOT NULL,
    doc_version INTEGER NOT NULL DEFAULT 0,
    doc_service_id BIGINT NOT NULL REFERENCES service(srv_id),
    doc_coach_id BIGINT REFERENCES coach(coa_id),
    doc_type VARCHAR NOT NULL,
    doc_file_extension VARCHAR NOT NULL,
    doc_created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    doc_modified_at TIMESTAMP WITH TIME ZONE,
    doc_created_by BIGINT NOT NULL REFERENCES users(usr_id),
    doc_modified_by BIGINT REFERENCES users(usr_id)
);

CREATE INDEX document_service_id_idx ON document(doc_service_id);
CREATE INDEX document_coach_id_idx ON document(doc_coach_id);