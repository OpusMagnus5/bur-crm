package pl.bodzioch.damian.user.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record FindUserByEmailQuery(
		String email

) implements Query<FindUserByEmailQueryResult> {
}
