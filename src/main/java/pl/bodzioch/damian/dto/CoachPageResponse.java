package pl.bodzioch.damian.dto;

import java.io.Serializable;
import java.util.List;

public record CoachPageResponse(
        List<CoachData> coaches,
        Long totalCoaches
) implements Serializable {
}
