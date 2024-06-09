package pl.bodzioch.damian.infrastructure.database;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CustomTypes {

    SERVICE_STATUS_DATA(null, null),
    DOCUMENT(null, "_documents");

    private final String paramName;
    private final String arrayParamName;

    public String asParamName() {
        if (paramName == null) {
            return DbCaster.PROPERTIES_PREFIX + this.name().toLowerCase();
        }
        return paramName;
    }

    public String asArrayParamName() {
        if (arrayParamName == null) {
            return DbCaster.PROPERTIES_PREFIX + this.name().toLowerCase();
        }
        return arrayParamName;
    }
}
