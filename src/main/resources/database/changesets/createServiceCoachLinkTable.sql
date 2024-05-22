CREATE TABLE service_coach(
    service_id BIGINT NOT NULL REFERENCES service(srv_id),
    coach_id BIGINT NOT NULL REFERENCES coach(coa_id)
);

CREATE INDEX service_coach_service_id_idx ON service_coach(service_id);