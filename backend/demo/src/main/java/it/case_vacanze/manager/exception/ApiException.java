package it.case_vacanze.manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

// Base per le eccezioni che il GlobalExceptionHandler trasforma in una risposta HTTP
public abstract class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @NonNull
    private final HttpStatus status;

    protected ApiException(@NonNull HttpStatus status, String messaggio) {
        super(messaggio);
        this.status = status;
    }

    @NonNull
    public HttpStatus getStatus() {
        return status;
    }
}
