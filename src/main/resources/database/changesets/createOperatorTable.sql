CREATE TABLE operator (
                        opr_id BIGSERIAL PRIMARY KEY,
                        opr_uuid UUID UNIQUE NOT NULL,
                        opr_version INTEGER NOT NULL,
                        opr_name VARCHAR UNIQUE NOT NULL,
                        opr_phone_number VARCHAR,
                        opr_created_at TIMESTAMP NOT NULL,
                        opr_modified_at TIMESTAMP,
                        opr_created_by BIGINT NOT NULL,
                        opr_modified_by BIGINT
);

CREATE UNIQUE INDEX operator_id_idx ON operator (opr_id);