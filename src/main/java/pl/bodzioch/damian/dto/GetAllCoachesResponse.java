package pl.bodzioch.damian.dto;

import java.io.Serializable;
import java.util.List;

public record GetAllCoachesResponse(
        List<CoachData> coaches
) implements Serializable {
}
