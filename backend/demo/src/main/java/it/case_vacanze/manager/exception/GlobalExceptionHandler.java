package it.case_vacanze.manager.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApi(ApiException ex) {
        return risposta(ex.getStatus(), ex.getMessage());
    }

    // Errori delle annotazioni @Valid sui DTO: restituisce un messaggio per ogni campo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidazione(MethodArgumentNotValidException ex) {
        Map<String, String> dettagli = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> dettagli.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Dati non validi", dettagli));
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErrorResponse> handleRichiestaMalformata(Exception ex) {
        return risposta(HttpStatus.BAD_REQUEST, "Richiesta non valida");
    }

    private ResponseEntity<ErrorResponse> risposta(@NonNull HttpStatus status, String messaggio) {
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), messaggio));
    }
}
