package it.case_vacanze.manager.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OffertaResponse(
        Integer id,
        @JsonProperty("data_inizio") LocalDate dataInizio,
        @JsonProperty("data_fine") LocalDate dataFine,
        @JsonProperty("prezzo_scontato") Double prezzoScontato,
        @JsonProperty("immagine_off") String immagine) {
}
