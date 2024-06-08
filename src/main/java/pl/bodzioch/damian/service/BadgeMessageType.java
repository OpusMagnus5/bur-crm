package pl.bodzioch.damian.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BadgeMessageType {
    NOT_COMPLETE_SERVICE("service.notCompleteInBur");

    private final String messageCode;
}

