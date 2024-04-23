package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record UserExistsResponse(

		boolean exists

) implements Serializable {
}
