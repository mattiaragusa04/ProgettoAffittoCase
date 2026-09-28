package it.case_vacanze.manager.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrazioneRequest(
        @NotBlank(message = "Il nome è obbligatorio") String nome,
        @NotBlank(message = "Il cognome è obbligatorio") String cognome,
        @NotBlank(message = "L'email è obbligatoria") @Email(message = "Email non valida") String email,
        @NotBlank(message = "La password è obbligatoria")
        @Size(max = 72, message = "La password può avere al massimo 72 caratteri") String password) {
}
