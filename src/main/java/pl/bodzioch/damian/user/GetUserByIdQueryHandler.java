package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.user.query_dto.GetUserByIdQuery;
import pl.bodzioch.damian.user.query_dto.GetUserByIdQueryResult;
import pl.bodzioch.damian.utils.MessageResolver;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetUserByIdQueryHandler implements QueryHandler<GetUserByIdQuery, GetUserByIdQueryResult> {

    private final IUserReadRepository readRepository;
    private final MessageResolver messageResolver;

    public Class<GetUserByIdQuery> queryClass() {
        return GetUserByIdQuery.class;
    }

    @Override
    public GetUserByIdQueryResult handle(GetUserByIdQuery query) {
        User user = readRepository.getById(query.id()).orElseThrow(() -> buildUserByIdNotFound(query.id()));
        return new GetUserByIdQueryResult(new UserDto(user, messageResolver));
    }

    private AppException buildUserByIdNotFound(Long id) {
        return new AppException(
                HttpStatus.NOT_FOUND,
                List.of(new ErrorData("error.client.userByIdNotFound", List.of(id.toString())))
        );
    }
}
