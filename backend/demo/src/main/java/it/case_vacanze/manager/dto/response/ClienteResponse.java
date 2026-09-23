package it.case_vacanze.manager.dto.response;

// Non contiene la password
public record ClienteResponse(Integer id, String nome, String cognome, String email, String picture) {
}
