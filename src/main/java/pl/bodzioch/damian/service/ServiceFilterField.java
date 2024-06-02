package pl.bodzioch.damian.service;

import lombok.RequiredArgsConstructor;
import pl.bodzioch.damian.utils.FilterField;

@RequiredArgsConstructor
public enum ServiceFilterField implements FilterField {
    NUMBER("_srv_number"),
    TYPE("_srv_type"),
    SERVICE_PROVIDER_ID("_srv_service_provider_id"),
    CUSTOMER_ID("_srv_customer_id");

    private final String dbName;

    @Override
    public String dbName() {
        return dbName;
    }
}
