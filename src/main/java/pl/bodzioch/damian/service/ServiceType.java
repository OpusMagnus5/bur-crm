package pl.bodzioch.damian.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.bodzioch.damian.exception.AppException;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ServiceType {

    TRAINING(144L),
    CONSULTING(150L);

    private final long burId;

    public static ServiceType ofBurId(Long burId) {
        return Arrays.stream(ServiceType.values())
                .filter(serviceType -> serviceType.getBurId() == burId)
                .findAny()
                .orElseThrow(() -> new AppException("There is no Service Type with bur id: " + burId));
    }
}
