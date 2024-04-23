package pl.bodzioch.damian.user.queryDto;

import pl.bodzioch.damian.infrastructure.query.Query;
import pl.bodzioch.damian.user.IdKind;

public record CheckUserExistenceQuery (

		IdKind idKind,
		String id

) implements Query<CheckUserExistenceQuerResult> {
}
