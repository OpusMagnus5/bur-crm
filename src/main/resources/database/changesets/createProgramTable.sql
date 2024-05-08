CREATE TABLE program(
    prg_id BIGSERIAL PRIMARY KEY,
    prg_uuid UUID UNIQUE NOT NULL,
    prg_version INTEGER NOT NULL,
    prg_name VARCHAR NOT NULL,
    prg_operator_id BIGINT NOT NULL REFERENCES operator(opr_id),
    prg_created_at TIMESTAMP NOT NULL,
    prg_modified_at TIMESTAMP,
    prg_created_by BIGINT NOT NULL,
    prg_modified_by BIGINT
);

CREATE INDEX program_name_idx ON program USING gin (to_tsvector('simple', prg_name));
CREATE INDEX program_operator_id ON program(prg_operator_id);