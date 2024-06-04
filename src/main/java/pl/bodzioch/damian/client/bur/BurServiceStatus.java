package pl.bodzioch.damian.client.bur;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum BurServiceStatus {
    @JsonProperty("OPUBLIKOWANA")
    PUBLISHED,
    @JsonProperty("ODWOLANA")
    CANCELED,
    @JsonProperty("ZAWIESZONA")
    SUSPENDED,
    @JsonProperty("ZREALIZOWANA")
    COMPLETED,
    @JsonProperty("ZABLOKOWANA")
    LOCKED,
    @JsonProperty("NIEZREALIZOWANA")
    NOT_PROVIDED
}
