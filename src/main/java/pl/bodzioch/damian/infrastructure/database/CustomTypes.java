package pl.bodzioch.damian.infrastructure.database;

public enum CustomTypes {

    SERVICE_STATUS_DATA;

    public String asParamName() {
        return DbCaster.PROPERTIES_PREFIX + this.name().toLowerCase();
    }
}
