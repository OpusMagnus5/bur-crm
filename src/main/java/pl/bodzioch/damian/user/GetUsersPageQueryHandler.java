package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.user.queryDto.GetUsersPageQuery;
import pl.bodzioch.damian.user.queryDto.GetUsersPageQueryResult;
import pl.bodzioch.damian.utils.MessageResolver;
import pl.bodzioch.damian.valueobject.PageQuery;
import pl.bodzioch.damian.valueobject.PageQueryResult;

import java.util.Comparator;
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
    public GetUsersPageQueryResult handle(GetUsersPageQuery command) {
        PageQuery pageQuery = new PageQuery(command.pageNumber(), command.pageSize());
        PageQueryResult<User> result = readRepository.getUsers(pageQuery);
        List<UserDto> users = result.elements().stream()
                .sorted(Comparator.comparing(User::usr_first_name).thenComparing(User::usr_last_name))
                .map(element -> new UserDto(element, messageResolver))
                .toList();
        return new GetUsersPageQueryResult(users, result.totalElements());
    }
}
