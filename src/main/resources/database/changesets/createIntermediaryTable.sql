CREATE TABLE intermediary(
    itr_id BIGSERIAL PRIMARY KEY,
    itr_uuid UUID UNIQUE NOT NULL,
    itr_version INTEGER NOT NULL DEFAULT 0,
    itr_name VARCHAR NOT NULL,
    itr_nip BIGINT UNIQUE NOT NULL,
    itr_created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    itr_modified_at TIMESTAMP WITH TIME ZONE,
    itr_created_by BIGINT NOT NULL REFERENCES users(usr_id),
    itr_modified_by BIGINT REFERENCES users(usr_id)
);

CREATE UNIQUE INDEX intermediary_nip_idx ON intermediary (itr_nip);
CREATE INDEX intermediary_name_idx ON intermediary USING gin (to_tsvector('simple', itr_name))