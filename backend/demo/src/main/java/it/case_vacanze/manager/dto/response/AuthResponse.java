package it.case_vacanze.manager.dto.response;

// Risposta di login e registrazione: il token da usare nelle richieste + i dati dell'utente
public record AuthResponse(String token, ClienteResponse utente) {
}
