CREATE TABLE coach(
    coa_id BIGSERIAL PRIMARY KEY,
    coa_uuid UUID UNIQUE NOT NULL,
    coa_version INTEGER NOT NULL DEFAULT 0,
    coa_first_name VARCHAR NOT NULL,
    coa_last_name VARCHAR NOT NULL,
    coa_pesel VARCHAR UNIQUE NOT NULL,
    coa_created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    coa_modified_at TIMESTAMP WITH TIME ZONE,
    coa_created_by BIGINT NOT NULL REFERENCES users(usr_id),
    coa_modified_by BIGINT REFERENCES users(usr_id)
);

CREATE INDEX coach_first_name_idx ON coach(coa_first_name);
CREATE INDEX coach_last_name_idx ON coach(coa_last_name);