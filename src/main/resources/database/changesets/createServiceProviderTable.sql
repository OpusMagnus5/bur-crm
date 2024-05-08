CREATE TABLE service_provider (
    spr_id BIGSERIAL PRIMARY KEY,
    spr_uuid UUID UNIQUE NOT NULL,
    spr_version INTEGER NOT NULL,
    spr_bur_id BIGINT UNIQUE,
    spr_name VARCHAR NOT NULL,
    spr_nip BIGINT UNIQUE NOT NULL,
    spr_created_at TIMESTAMP NOT NULL,
    spr_modified_at TIMESTAMP,
    spr_created_by BIGINT NOT NULL,
    spr_modified_by BIGINT
);

CREATE UNIQUE INDEX service_provider_nip_idx ON service_provider (spr_nip);