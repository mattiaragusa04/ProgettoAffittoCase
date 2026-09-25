package it.case_vacanze.manager.dto.response;

import it.case_vacanze.manager.entity.Ruolo;

// Non contiene la password
public record ClienteResponse(Integer id, String nome, String cognome, String email, String picture, Ruolo ruolo) {
}
