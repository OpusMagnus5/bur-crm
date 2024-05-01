package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.user.query_dto.CheckUserExistenceQuerResult;
import pl.bodzioch.damian.user.query_dto.CheckUserExistenceQuery;

@Component
@RequiredArgsConstructor
class CheckUserExistenceQueryHandler implements QueryHandler<CheckUserExistenceQuery, CheckUserExistenceQuerResult> {

	private final IUserReadRepository readRepository;

	@Override
	public Class<CheckUserExistenceQuery> queryClass() {
		return CheckUserExistenceQuery.class;
	}

	@Override
	public CheckUserExistenceQuerResult handle(CheckUserExistenceQuery query) {
		boolean present = false;
		if (query.userIdKind() == UserIdKind.EMAIL) {
			present = readRepository.getByEmail(query.id()).isPresent();
		}
		return new CheckUserExistenceQuerResult(present);
	}
}
