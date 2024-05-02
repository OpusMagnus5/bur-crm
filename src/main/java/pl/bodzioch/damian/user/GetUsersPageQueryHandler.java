package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.user.query_dto.GetUsersPageQuery;
import pl.bodzioch.damian.user.query_dto.GetUsersPageQueryResult;
import pl.bodzioch.damian.utils.MessageResolver;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetUsersPageQueryHandler implements QueryHandler<GetUsersPageQuery, GetUsersPageQueryResult> {

    private final IUserReadRepository readRepository;
    private final MessageResolver messageResolver;

    @Override
    public Class<GetUsersPageQuery> queryClass() {
        return GetUsersPageQuery.class;
    }

    @Override
    public GetUsersPageQueryResult handle(GetUsersPageQuery query) {
        PageQuery pageQuery = new PageQuery(query.pageNumber(), query.pageSize());
        PageQueryResult<User> result = readRepository.getUsers(pageQuery);
        List<UserDto> users = result.elements().stream()
                .map(element -> new UserDto(element, messageResolver))
                .toList();
        return new GetUsersPageQueryResult(users, result.totalElements());
    }
}
