package pl.bodzioch.damian.user.queryDto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetUserByIdQuery(

		Long id
) implements Query<GetUserByIdQueryResult> {
}
