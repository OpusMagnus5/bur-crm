package pl.bodzioch.damian.dto;

import java.io.Serializable;
import java.util.List;

public record GetAllServiceTypesResponse(
        List<ServiceTypeData> serviceTypes
) implements Serializable {
}
