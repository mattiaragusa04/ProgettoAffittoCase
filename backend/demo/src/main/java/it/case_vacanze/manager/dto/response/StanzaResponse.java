package it.case_vacanze.manager.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StanzaResponse(
        Integer id,
        Integer numero,
        @JsonProperty("numero_posti") Integer numeroPosti,
        Boolean disponibile,
        Double prezzo,
        String immagine,
        String descrizione,
        String tipo,
        Double valutazione,
        @JsonProperty("codice_off") Integer codiceOfferta) {
}
