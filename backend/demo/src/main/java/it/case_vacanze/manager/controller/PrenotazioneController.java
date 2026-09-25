package it.case_vacanze.manager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.case_vacanze.manager.dto.request.PrenotazioneRequest;
import it.case_vacanze.manager.dto.response.PrenotazioneResponse;
import it.case_vacanze.manager.services.PrenotazioneService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/prenotazione")
public class PrenotazioneController {

    private final PrenotazioneService prenotazioneService;

    public PrenotazioneController(PrenotazioneService prenotazioneService) {
        this.prenotazioneService = prenotazioneService;
    }

    @GetMapping
    public List<PrenotazioneResponse> getAllPrenotazioni() {
        return prenotazioneService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrenotazioneResponse createPrenotazione(@Valid @RequestBody PrenotazioneRequest req) {
        return prenotazioneService.crea(req);
    }

    // Prenotazioni dell'utente loggato: l'utente arriva dal token, non dal client
    @GetMapping("/mie")
    public List<PrenotazioneResponse> getMiePrenotazioni() {
        return prenotazioneService.findMie();
    }
}
