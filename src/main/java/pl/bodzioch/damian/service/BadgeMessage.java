package pl.bodzioch.damian.service;

import java.util.List;

public record BadgeMessage(
        BadgeMessageType type,
        List<String> params
) {

    public BadgeMessage(BadgeMessageType type) {
        this(
                type,
                List.of()
        );
    }
}
