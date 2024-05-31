package pl.bodzioch.damian.dto;

import java.io.Serializable;
import java.util.List;

public record ServicePageResponse(
        List<ServiceData> services,
        Long totalServices
) implements Serializable {
}
