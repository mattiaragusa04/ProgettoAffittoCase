package it.case_vacanze.manager.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OffertaRequest(
        @JsonProperty("data_inizio") @NotNull(message = "La data di inizio è obbligatoria") LocalDate dataInizio,
        @JsonProperty("data_fine") @NotNull(message = "La data di fine è obbligatoria") LocalDate dataFine,
        @JsonProperty("prezzo_scontato") @NotNull(message = "Il prezzo è obbligatorio") @Positive(message = "Il prezzo deve essere maggiore di zero") Double prezzoScontato,
        @JsonProperty("immagine_off") String immagine) {
}
