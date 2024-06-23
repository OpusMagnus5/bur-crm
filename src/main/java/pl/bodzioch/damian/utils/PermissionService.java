package pl.bodzioch.damian.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import pl.bodzioch.damian.configuration.security.SecurityConstants;
import pl.bodzioch.damian.user.UserRole;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final CipherComponent cipherComponent;

    public boolean isRequestingOwnUser(String requestedId) {
        long parsedReqId = cipherComponent.getDecryptedId(requestedId);
        long parsedPrincipalId = cipherComponent.getPrincipalId();
        return parsedReqId == parsedPrincipalId;
    }

    public boolean hasTheSameRoleOrHigher(List<UserRole> requestedUser) {
        UserRole role = getRoles().getLast();
        return requestedUser.stream()
                .allMatch(r -> r.getHierarchy() <= role.getHierarchy());
    }

    public List<UserRole> getRoles() {
        Jwt token = getToken();
        return Arrays.stream(((String) token.getClaim(SecurityConstants.ROLES_CLAIM)).split(SecurityConstants.AUTHORITIES_CLAIM_DELIMITER))
                .map(UserRole::valueOf)
                .sorted(Comparator.comparing(UserRole::getHierarchy))
                .toList();
    }

    private Jwt getToken() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
