package pl.bodzioch.damian.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    BLOCKED_USER(0),
    USER(1),
    MANAGER(2),
    ADMIN(3);

    private final int hierarchy;
}
