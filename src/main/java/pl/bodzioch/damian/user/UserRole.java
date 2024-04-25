package pl.bodzioch.damian.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    USER(1),
    MANAGER(2),
    ADMIN(3);

    private final int hierarchy;
}
