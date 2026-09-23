package it.case_vacanze.manager.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record RecensioneRequest(
        @NotBlank(message = "Il testo è obbligatorio") @Size(max = 255, message = "Massimo 255 caratteri") String testo,
        @NotNull(message = "La valutazione è obbligatoria") @Min(value = 1, message = "La valutazione minima è 1") @Max(value = 5, message = "La valutazione massima è 5") Integer valutazione,
        @JsonProperty("cliente_id") @Positive(message = "Il cliente è obbligatorio") int clienteId) {
}
