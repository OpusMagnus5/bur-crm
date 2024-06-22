package pl.bodzioch.damian.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final CipherComponent cipherComponent;

    public boolean isRequestingOwnUser(String requestedId) {
        long parsedReqId = cipherComponent.getDecryptedId(requestedId);
        long parsedPrincipalId = cipherComponent.getPrincipalId();
        return parsedReqId == parsedPrincipalId;
    }
}
