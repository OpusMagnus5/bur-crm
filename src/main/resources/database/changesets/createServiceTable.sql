CREATE TABLE service(
    srv_id BIGSERIAL PRIMARY KEY,
    srv_uuid UUID UNIQUE NOT NULL,
    srv_version INTEGER NOT NULL DEFAULT 0,
    srv_bur_card_id BIGINT,
    srv_number VARCHAR NOT NULL,
    srv_name VARCHAR NOT NULL,
    srv_type VARCHAR NOT NULL,
    srv_start_date DATE NOT NULL,
    srv_end_date DATE NOT NULL,
    srv_number_of_participants INTEGER NOT NULL,
    srv_service_provider_id BIGINT NOT NULL REFERENCES service_provider(spr_id),
    srv_program_id BIGINT NOT NULL REFERENCES program(prg_id),
    srv_customer_id BIGINT NOT NULL REFERENCES customer(cst_id),
    srv_intermediary_id BIGINT NOT NULL REFERENCES intermediary(itr_id),
    srv_created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    srv_modified_at TIMESTAMP WITH TIME ZONE,
    srv_created_by BIGINT NOT NULL REFERENCES users(usr_id),
    srv_modified_by BIGINT REFERENCES users(usr_id)
);

CREATE INDEX service_name_idx ON service USING gin (to_tsvector('simple', srv_name));
