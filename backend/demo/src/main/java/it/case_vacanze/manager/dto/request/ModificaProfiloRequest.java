package it.case_vacanze.manager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Dati modificabili dalla pagina "Dati personali" (l'email no: identifica l'utente nel token)
public record ModificaProfiloRequest(
        @NotBlank(message = "Il nome è obbligatorio") @Size(max = 255, message = "Massimo 255 caratteri") String nome,
        @NotBlank(message = "Il cognome è obbligatorio") @Size(max = 255, message = "Massimo 255 caratteri") String cognome) {
}
