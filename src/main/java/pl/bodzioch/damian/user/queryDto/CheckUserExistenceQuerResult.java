package pl.bodzioch.damian.user.queryDto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;

public record CheckUserExistenceQuerResult(

		boolean exists

)  implements QueryResult {
}
