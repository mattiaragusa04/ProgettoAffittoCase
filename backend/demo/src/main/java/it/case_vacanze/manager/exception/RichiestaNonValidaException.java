package it.case_vacanze.manager.exception;

import org.springframework.http.HttpStatus;

public class RichiestaNonValidaException extends ApiException {

    private static final long serialVersionUID = 1L;

    public RichiestaNonValidaException(String messaggio) {
        super(HttpStatus.BAD_REQUEST, messaggio);
    }
}
