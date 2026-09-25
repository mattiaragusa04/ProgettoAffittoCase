package it.case_vacanze.manager.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.case_vacanze.manager.dto.response.StanzaResponse;
import it.case_vacanze.manager.services.StanzaService;

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
}
