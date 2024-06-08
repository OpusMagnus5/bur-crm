package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.service.BadgeMessageType;
import pl.bodzioch.damian.utils.MessageResolver;

public record BadgeMessageData(
        String type,
        String message
) {
    public BadgeMessageData(BadgeMessageType badgeMessage, MessageResolver messageResolver) {
        this(
                badgeMessage.name(),
                messageResolver.getMessage(badgeMessage.getMessageCode())
        );
    }
}
