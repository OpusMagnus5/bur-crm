package pl.bodzioch.damian.client.bur;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

record ServiceBur(
		Long id,
		@JsonProperty("numer")
		String number,
		@JsonProperty("tytul")
		String title,
		@JsonProperty("idRodzajuUslugi")
		Long serviceTypeId,
		@JsonProperty("dataRozpoczeciaUslugi")
		LocalDateTime startDate,
		@JsonProperty("dataZakonczeniaUslugi")
		LocalDateTime endDate,
		@JsonProperty("dostawcaUslug")
		ServiceProviderBur serviceProvider
) {
}
