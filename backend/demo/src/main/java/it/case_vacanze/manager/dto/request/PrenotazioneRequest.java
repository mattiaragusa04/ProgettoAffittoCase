package it.case_vacanze.manager.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// Il prezzo totale non arriva dal client: lo calcola il service
public record PrenotazioneRequest(
        @JsonProperty("data_check_in") @NotNull(message = "La data di check-in è obbligatoria") @FutureOrPresent(message = "Il check-in non può essere nel passato") LocalDate checkIn,
        @JsonProperty("data_check_out") @NotNull(message = "La data di check-out è obbligatoria") LocalDate checkOut,
        @JsonProperty("numero_persone") @NotNull(message = "Il numero di persone è obbligatorio") @Positive(message = "Serve almeno una persona") Integer numeroPersone,
        @JsonProperty("cliente_id") @Positive(message = "Il cliente è obbligatorio") int clienteId,
        @JsonProperty("stanza_id") @Positive(message = "La stanza è obbligatoria") int stanzaId) {
}
