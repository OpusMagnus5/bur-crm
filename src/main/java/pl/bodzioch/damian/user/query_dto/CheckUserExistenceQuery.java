package pl.bodzioch.damian.user.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;
import pl.bodzioch.damian.user.UserIdKind;

public record CheckUserExistenceQuery (

		UserIdKind userIdKind,
		String id

) implements Query<CheckUserExistenceQuerResult> {
}
