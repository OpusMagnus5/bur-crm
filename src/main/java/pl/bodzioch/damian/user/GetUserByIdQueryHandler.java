package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.user.query_dto.GetUserByIdQuery;
import pl.bodzioch.damian.user.query_dto.GetUserByIdQueryResult;
import pl.bodzioch.damian.utils.MessageResolver;

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
        User user = readRepository.getById(query.id()).orElseThrow(() -> User.buildUserByIdNotFound(query.id()));
        return new GetUserByIdQueryResult(new UserDto(user, messageResolver));
    }
}
