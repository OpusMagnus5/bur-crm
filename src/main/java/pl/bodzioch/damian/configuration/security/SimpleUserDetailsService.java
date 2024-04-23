package pl.bodzioch.damian.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.user.UserDto;
import pl.bodzioch.damian.user.queryDto.GetUserByEmailQuery;

@Component
@RequiredArgsConstructor
class SimpleUserDetailsService implements UserDetailsService {

    private final QueryExecutor queryExecutor;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        GetUserByEmailQuery query = new GetUserByEmailQuery(username);
        UserDto result = queryExecutor.execute(query).userDto();
        return UserDetailsDto.builder()
                .id(result.id())
                .email(result.email())
                .password(result.password())
                .roles(result.roles())
                .build();
    }
}
