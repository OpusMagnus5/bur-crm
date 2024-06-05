package pl.bodzioch.damian.service;

import pl.bodzioch.damian.client.bur.BurServiceStatus;

public enum ServiceStatus {
    PUBLISHED,
    CANCELED,
    SUSPENDED,
    COMPLETED,
    LOCKED,
    NOT_PROVIDED;

    public static ServiceStatus of(BurServiceStatus status) {
        return ServiceStatus.valueOf(status.name());
    }
}
