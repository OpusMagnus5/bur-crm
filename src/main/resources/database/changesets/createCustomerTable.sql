CREATE TABLE customer(
    cst_id BIGSERIAL PRIMARY KEY,
    cst_uuid UUID UNIQUE NOT NULL,
    cst_version INTEGER NOT NULL DEFAULT 0,
    cst_name VARCHAR NOT NULL,
    cst_nip BIGINT UNIQUE NOT NULL,
    cst_created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    cst_modified_at TIMESTAMP WITH TIME ZONE,
    cst_created_by BIGINT NOT NULL REFERENCES users(usr_id),
    cst_modified_by BIGINT REFERENCES users(usr_id)
);

CREATE UNIQUE INDEX customer_nip_idx ON customer (cst_nip);
CREATE INDEX customer_name_idx ON customer USING gin (to_tsvector('simple', cst_name))