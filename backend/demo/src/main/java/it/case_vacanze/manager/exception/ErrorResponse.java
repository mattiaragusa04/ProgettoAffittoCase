package it.case_vacanze.manager.exception;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponse(int status, String messaggio, Map<String, String> dettagli) {

    public ErrorResponse(int status, String messaggio) {
        this(status, messaggio, Map.of());
    }
}
