package pl.bodzioch.damian.error;

import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbId;
import pl.bodzioch.damian.infrastructure.database.DbManyToOne;
import pl.bodzioch.damian.user.InnerUser;

import java.time.LocalDateTime;
import java.util.UUID;

record Error(
       @DbId
       Long id,
       @DbColumn(name = "err_uuid")
       UUID uuid,
       @DbColumn(name = "err_user_id")
       Long userId,
       @DbColumn(name = "err_date")
       LocalDateTime date,
       @DbColumn(name = "err_class")
       String clazz,
       @DbColumn(name = "err_message")
       String message,
       @DbColumn(name = "err_stacktrace")
       String stacktrace,
       @DbColumn(name = "err_is_web")
       Boolean isWeb,
       @DbManyToOne(prefix = "user")
       InnerUser user
) {

    @DbConstructor
    Error {
    }

    Error(ErrorDto errorDto) {
        this(
                errorDto.id(), errorDto.uuid(), errorDto.userId(), errorDto.date(), errorDto.clazz(), errorDto.message(),
                errorDto.stacktrace(), errorDto.isWeb(), null
        );
    }
}
