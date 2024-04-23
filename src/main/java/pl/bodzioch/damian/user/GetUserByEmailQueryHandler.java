package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.user.queryDto.GetUserByEmailQuery;
import pl.bodzioch.damian.user.queryDto.GetUserByEmailQueryResult;
import pl.bodzioch.damian.valueobject.ErrorData;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetUserByEmailQueryHandler implements QueryHandler<GetUserByEmailQuery, GetUserByEmailQueryResult> {

    private final IUserReadRepository readRepository;

    public Class<GetUserByEmailQuery> queryClass() {
        return GetUserByEmailQuery.class;
    }

    @Override
    public GetUserByEmailQueryResult handle(GetUserByEmailQuery command) {
        User user = readRepository.getByEmail(command.email()).orElseThrow(() -> buildUserByEmailNotFound(command));
        return new GetUserByEmailQueryResult(new UserDto(user));
    }

    private AppException buildUserByEmailNotFound(GetUserByEmailQuery command) {
        return new AppException(
                HttpStatus.NOT_FOUND,
                List.of(new ErrorData("error.client.userByEmailNotFound", List.of(command.email())))
        );
    }
}
