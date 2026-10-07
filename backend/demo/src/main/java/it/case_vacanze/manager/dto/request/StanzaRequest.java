package it.case_vacanze.manager.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

// La valutazione non arriva dal client (nasce a 0, poi dipende dalle recensioni)
// e la stanza nasce sempre prenotabile
public record StanzaRequest(
        @NotNull(message = "Il numero della stanza è obbligatorio") @Positive(message = "Il numero della stanza deve essere maggiore di zero") Integer numero,
        @JsonProperty("numero_posti") @NotNull(message = "Il numero di posti è obbligatorio") @Positive(message = "Serve almeno un posto") Integer numeroPosti,
        @NotNull(message = "Il prezzo è obbligatorio") @Positive(message = "Il prezzo deve essere maggiore di zero") Double prezzo,
        @NotBlank(message = "L'immagine è obbligatoria") @Size(max = 1000, message = "L'indirizzo dell'immagine è troppo lungo") String immagine,
        @NotBlank(message = "La descrizione è obbligatoria") @Size(max = 255, message = "La descrizione può avere al massimo 255 caratteri") String descrizione,
        @NotBlank(message = "Il tipo è obbligatorio") @Size(max = 255, message = "Il tipo può avere al massimo 255 caratteri") String tipo,
        @JsonProperty("codice_off") Integer codiceOfferta) {
}
