package it.case_vacanze.manager.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.case_vacanze.manager.dto.request.StanzaRequest;
import it.case_vacanze.manager.dto.response.StanzaResponse;
import it.case_vacanze.manager.services.StanzaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/stanze")
public class StanzeController {

    private final StanzaService stanzaService;

    public StanzeController(StanzaService stanzaService) {
        this.stanzaService = stanzaService;
    }

    @GetMapping
    public List<StanzaResponse> getAllStanze() {
        return stanzaService.findAll();
    }

    @GetMapping("/{id}")
    public StanzaResponse getStanza(@PathVariable int id) {
        return stanzaService.trova(id);
    }

    @GetMapping("/search")
    public List<StanzaResponse> searchStanze(
            @RequestParam("checkIn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam("checkOut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam("guests") Integer guests) {
        return stanzaService.cercaDisponibili(checkIn, checkOut, guests);
    }

    // Solo l'amministratore (regola in SecurityConfig)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StanzaResponse creaStanza(@Valid @RequestBody StanzaRequest req) {
        return stanzaService.crea(req);
    }

    // Solo l'amministratore (regola in SecurityConfig)
    @PostMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancellaStanza(@PathVariable int id) {
        stanzaService.cancella(id);
    }
}
