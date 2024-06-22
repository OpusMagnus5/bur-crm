package pl.bodzioch.damian.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;

import static pl.bodzioch.damian.user.GenerateJwtTokenCommandHandler.PRINCIPAL_ID;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final CipherComponent cipherComponent;

    public boolean isRequestingOwnUser(String requestedId, Principal principal) {
        String principalId = ((JwtAuthenticationToken) principal).getToken().getClaim(PRINCIPAL_ID);
        long parsedReqId = cipherComponent.getDecryptedId(requestedId);
        long parsedPrincipalId = cipherComponent.getDecryptedId(principalId);
        return parsedReqId == parsedPrincipalId;
    }
}
