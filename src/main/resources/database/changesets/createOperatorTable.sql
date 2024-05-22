CREATE TABLE operator (
                        opr_id BIGSERIAL PRIMARY KEY,
                        opr_uuid UUID UNIQUE NOT NULL,
                        opr_version INTEGER NOT NULL DEFAULT 0,
                        opr_name VARCHAR UNIQUE NOT NULL,
                        opr_notes VARCHAR,
                        opr_created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
                        opr_modified_at TIMESTAMP WITH TIME ZONE,
                        opr_created_by BIGINT NOT NULL REFERENCES users(usr_id),
                        opr_modified_by BIGINT REFERENCES users(usr_id)
);

CREATE INDEX operator_name_idx ON operator USING gin (to_tsvector('simple', opr_name))