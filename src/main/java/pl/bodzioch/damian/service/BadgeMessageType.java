package pl.bodzioch.damian.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BadgeMessageType {
    NOT_COMPLETE_SERVICE("service.notCompleteInBur"),
    MISSING_REPORT("service.missingReport"),
    NOT_ENOUGH_CONSENTS("service.notEnoughConsents"),
    MISSING_COACH_INVOICE("service.missingCoachInvoice"),
    MISSING_PROVIDER_INVOICE("service.missingProviderInvoice"),
    MISSING_INTERMEDIARY_INVOICE("service.missingIntermediaryInvoice"),
    NOT_ENOUGH_PARTICIPANT_BUR_QUESTIONNAIRES("service.notEnoughParticipantBurQuestionnaires"),
    MISSING_CUSTOMER_BUR_QUESTIONNAIRE("service.missingCustomerBurQuestionnaire"),
    NOT_ENOUGH_PARTICIPANT_PROVIDER_QUESTIONNAIRE("service.notEnoughParticipantProviderQuestionnaires"),
    MISSING_ATTENDANCE_LIST("service.missingAttendanceList"),
    MISSING_PRESENTATION("service.missingPresentation");

    private final String messageCode;
}

