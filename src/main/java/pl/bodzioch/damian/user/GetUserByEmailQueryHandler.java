package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.user.query_dto.GetUserByEmailQuery;
import pl.bodzioch.damian.user.query_dto.GetUserByEmailQueryResult;
import pl.bodzioch.damian.utils.MessageResolver;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetUserByEmailQueryHandler implements QueryHandler<GetUserByEmailQuery, GetUserByEmailQueryResult> {

    private final IUserReadRepository readRepository;
    private final MessageResolver messageResolver;

    public Class<GetUserByEmailQuery> queryClass() {
        return GetUserByEmailQuery.class;
    }

    @Override
    public GetUserByEmailQueryResult handle(GetUserByEmailQuery query) {
        User user = readRepository.getByEmail(query.email()).orElseThrow(() -> buildUserByEmailNotFound(query));
        return new GetUserByEmailQueryResult(new UserDto(user, messageResolver));
    }

    private AppException buildUserByEmailNotFound(GetUserByEmailQuery query) {
        return new AppException(
                HttpStatus.NOT_FOUND,
                List.of(new ErrorData("error.client.userByEmailNotFound", List.of(query.email())))
        );
    }
}
