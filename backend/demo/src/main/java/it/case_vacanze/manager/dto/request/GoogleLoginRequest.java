package it.case_vacanze.manager.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// Dati restituiti da https://www.googleapis.com/oauth2/v3/userinfo
public record GoogleLoginRequest(
        @NotBlank(message = "L'email è obbligatoria") @Email(message = "Email non valida") String email,
        @JsonProperty("given_name") String nome,
        @JsonProperty("family_name") String cognome,
        String picture) {
}
