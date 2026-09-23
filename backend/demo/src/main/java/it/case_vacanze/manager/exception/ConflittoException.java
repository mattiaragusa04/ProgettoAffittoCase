package it.case_vacanze.manager.exception;

import org.springframework.http.HttpStatus;

public class ConflittoException extends ApiException {

    private static final long serialVersionUID = 1L;

    public ConflittoException(String messaggio) {
        super(HttpStatus.CONFLICT, messaggio);
    }
}
