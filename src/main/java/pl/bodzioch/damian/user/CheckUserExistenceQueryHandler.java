package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.user.queryDto.CheckUserExistenceQuerResult;
import pl.bodzioch.damian.user.queryDto.CheckUserExistenceQuery;

@RequiredArgsConstructor
public class CheckUserExistenceQueryHandler implements QueryHandler<CheckUserExistenceQuery, CheckUserExistenceQuerResult> {

	private final IUserReadRepository readRepository;

	@Override
	public Class<CheckUserExistenceQuery> queryClass() {
		return CheckUserExistenceQuery.class;
	}

	@Override
	public CheckUserExistenceQuerResult handle(CheckUserExistenceQuery command) {
		boolean present = false;
		if (command.idKind() == IdKind.EMAIL) {
			present = readRepository.getByEmail(command.id()).isPresent();
		}
		return new CheckUserExistenceQuerResult(present);
	}
}
