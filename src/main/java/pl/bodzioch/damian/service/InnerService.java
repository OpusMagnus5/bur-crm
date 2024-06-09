package pl.bodzioch.damian.service;

import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbId;

public record InnerService(
        @DbId
        @DbColumn(name = "srv_id")
        Long id,
        @DbColumn(name = "srv_type")
        ServiceType type,
        @DbColumn(name = "srv_number_of_participants")
        Integer numberOfParticipants
) {
    @DbConstructor
    public InnerService {
    }
}
