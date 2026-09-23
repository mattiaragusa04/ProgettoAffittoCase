package it.case_vacanze.manager.exception;

import org.springframework.http.HttpStatus;

public class CredenzialiNonValideException extends ApiException {

    private static final long serialVersionUID = 1L;

    public CredenzialiNonValideException(String messaggio) {
        super(HttpStatus.UNAUTHORIZED, messaggio);
    }
}
