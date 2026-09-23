package it.case_vacanze.manager.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RecensioneResponse(
        Integer id,
        String testo,
        Integer valutazione,
        @JsonProperty("cliente_id") Integer clienteId,
        AutoreResponse cliente) {
}
