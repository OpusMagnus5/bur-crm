package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.service.BadgeMessage;
import pl.bodzioch.damian.utils.MessageResolver;

public record BadgeMessageData(
        String type,
        String message
) {
    public BadgeMessageData(BadgeMessage badgeMessage, MessageResolver messageResolver) {
        this(
                badgeMessage.type().name(),
                messageResolver.getMessage(badgeMessage.type().getMessageCode(), badgeMessage.params())
        );
    }
}
