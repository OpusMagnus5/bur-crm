package pl.bodzioch.damian.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import pl.bodzioch.damian.configuration.security.SecurityConstants;
import pl.bodzioch.damian.user.UserRole;

import java.util.*;

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
        Jwt token = getToken().get();
        return Arrays.stream(((String) token.getClaim(SecurityConstants.ROLES_CLAIM)).split(SecurityConstants.AUTHORITIES_CLAIM_DELIMITER))
                .map(UserRole::valueOf)
                .sorted(Comparator.comparing(UserRole::getHierarchy))
                .toList();
    }

    public Optional<UUID> getSessionId() {
        return getToken().map(token -> token.getClaim(SecurityConstants.SESSION_ID))
                .map(sessionId -> UUID.fromString((String) sessionId));
    }

    private Optional<Jwt> getToken() {
        Object jwt = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (jwt instanceof Jwt) {
            return Optional.of((Jwt) jwt);
        }
        return Optional.empty();
    }
}
