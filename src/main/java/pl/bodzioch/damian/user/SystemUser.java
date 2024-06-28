package pl.bodzioch.damian.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SystemUser {

    ADMIN(-1),
    STATUS_SYNCHRONIZER(-2);

    private final long id;
}
