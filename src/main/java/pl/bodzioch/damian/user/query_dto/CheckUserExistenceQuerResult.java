package pl.bodzioch.damian.user.query_dto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;

public record CheckUserExistenceQuerResult(

		boolean exists

)  implements QueryResult {
}
