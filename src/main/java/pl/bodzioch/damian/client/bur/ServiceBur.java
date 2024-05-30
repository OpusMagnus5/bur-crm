package pl.bodzioch.damian.client.bur;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

record ServiceBur(
		Long id,
		@JsonProperty("numer")
		String number,
		@JsonProperty("tytul")
		String title,
		@JsonProperty("idRodzajuUslugi")
		Long serviceTypeId,
		@JsonProperty("dataRozpoczeciaUslugi")
		ZonedDateTime startDate,
		@JsonProperty("dataZakonczeniaUslugi")
		ZonedDateTime endDate,
		@JsonProperty("dostawcaUslug")
		ServiceProviderBur serviceProvider
) {
}
