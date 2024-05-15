package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.user.query_dto.FindUserByEmailQuery;
import pl.bodzioch.damian.user.query_dto.FindUserByEmailQueryResult;
import pl.bodzioch.damian.utils.MessageResolver;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@RequiredArgsConstructor
class FindUserByEmailQueryHandler implements QueryHandler<FindUserByEmailQuery, FindUserByEmailQueryResult> {

	private final IUserReadRepository readRepository;
	private final MessageResolver messageResolver;

	@Override
	public Class<FindUserByEmailQuery> queryClass() {
		return FindUserByEmailQuery.class;
	}

	@Override
	public FindUserByEmailQueryResult handle(FindUserByEmailQuery query) {
		User user = readRepository.getByEmail(query.email()).orElseThrow(() -> buildUserByEmailNotFound(query.email()));
		return new FindUserByEmailQueryResult(new UserDto(user, messageResolver));
	}

	private AppException buildUserByEmailNotFound(String email) {
		return new AppException(
				"User with email: " + email + " not found",
				HttpStatus.NOT_FOUND,
				List.of(new ErrorData("error.client.userByEmailNotFound", List.of(email)))
		);
	}
}
