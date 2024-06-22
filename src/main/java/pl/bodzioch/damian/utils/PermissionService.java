package pl.bodzioch.damian.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import pl.bodzioch.damian.user.UserRole;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static pl.bodzioch.damian.user.GenerateJwtTokenCommandHandler.AUTHORITIES_CLAIM_DELIMITER;
import static pl.bodzioch.damian.user.GenerateJwtTokenCommandHandler.ROLES_CLAIM;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final CipherComponent cipherComponent;

    public boolean isRequestingOwnUser(String requestedId) {
        long parsedReqId = cipherComponent.getDecryptedId(requestedId);
        long parsedPrincipalId = cipherComponent.getPrincipalId();
        return parsedReqId == parsedPrincipalId;
    }

    public List<UserRole> getRoles() {
        Jwt token = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Arrays.stream(((String) token.getClaim(ROLES_CLAIM)).split(AUTHORITIES_CLAIM_DELIMITER))
                .map(UserRole::valueOf)
                .sorted(Comparator.comparing(UserRole::getHierarchy))
                .toList();
    }
}
