package it.case_vacanze.manager.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NewsletterRequest(
        @NotBlank(message = "L'email è obbligatoria") @Email(message = "Email non valida") String email) {
}
