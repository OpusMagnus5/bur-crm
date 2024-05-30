package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.UniqueElements;
import pl.bodzioch.damian.service.validator.ServiceTypeV;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record CreateNewServiceRequest(
        @NotEmpty(message = "error.client.service.numberEmpty")
        @Pattern(regexp = "\\d{4}/\\d{2}/\\d{2}/\\d+/\\d+", message = "error.client.service.incorrectNumber")
        String number,
        @NotEmpty(message = "error.client.service.nameEmpty")
        @Pattern(regexp = "[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9: -/.\"\\\\]{1,300}", message = "error.client.service.incorrectName")
        String name,
        @NotEmpty(message = "error.client.service.typeEmpty")
        @ServiceTypeV(message = "error.client.service.incorrectType")
        String type,
        @NotNull(message = "error.client.service.emptyStartDate")
        LocalDate startDate,
        @NotNull(message = "error.client.service.emptyEndDate")
        LocalDate endDate,
        @NotNull(message = "error.client.service.emptyNumberOfParticipants")
        @Min(value = 1, message = "error.client.service.incorrectNumberOfParticipants")
        @Max(value = Integer.MAX_VALUE, message = "error.client.service.incorrectNumberOfParticipants")
        Integer numberOfParticipants,
        @NotEmpty(message = "error.client.service.emptyServiceProviderId")
        String serviceProviderId,
        @NotEmpty(message = "error.client.service.emptyProgramId")
        String programId,
        @NotEmpty(message = "error.client.service.emptyCustomerId")
        String customerId,
        @UniqueElements(message = "error.client.service.uniqueCoachesIds")
        @NotEmpty(message = "error.client.service.emptyCoachesIds")
        List<String> coachIds,
        @NotEmpty(message = "error.client.service.emptyIntermediaryId")
        String intermediaryId
) implements Serializable {
}
