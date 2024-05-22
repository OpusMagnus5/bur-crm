package pl.bodzioch.damian.client.bur;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.bodzioch.damian.exception.HttpClientException;

import java.util.List;

record ServiceResponse(
		@JsonProperty("lista")
		List<ServiceBur> services
) {

	BurServiceDto mapFirst() {
		return services.stream()
				.map(BurServiceDto::new)
				.findFirst()
				.orElseThrow(() -> new HttpClientException("Empty service in response"));
	}
}
