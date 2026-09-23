package it.case_vacanze.manager.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PrenotazioneResponse(
        Integer id,
        @JsonProperty("data_check_in") LocalDate checkIn,
        @JsonProperty("data_check_out") LocalDate checkOut,
        @JsonProperty("numero_persone") Integer numeroPersone,
        @JsonProperty("prezzo_totale") Double prezzoTotale,
        @JsonProperty("cliente_id") Integer clienteId,
        @JsonProperty("stanza_id") Integer stanzaId) {
}
