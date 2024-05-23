package pl.bodzioch.damian.service_provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.bodzioch.damian.exception.AppException;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum BurServiceProvider {
    OPUS_MAGNUS(137771L),
    BOOKINGANIMALSPA(124220L),
    BTLA_SKILLS(46039L);

    private final Long burId;

    public static BurServiceProvider ofBurId(long burId) {
        return Arrays.stream(BurServiceProvider.values())
                .filter(provider -> provider.getBurId() == burId)
                .findAny()
                .orElseThrow(() -> new AppException("There is no BurServiceProvider with bur id: " + burId));
    }
}
