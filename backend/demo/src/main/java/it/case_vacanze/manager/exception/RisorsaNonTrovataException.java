package it.case_vacanze.manager.exception;

import org.springframework.http.HttpStatus;

public class RisorsaNonTrovataException extends ApiException {

    private static final long serialVersionUID = 1L;

    public RisorsaNonTrovataException(String messaggio) {
        super(HttpStatus.NOT_FOUND, messaggio);
    }
}
