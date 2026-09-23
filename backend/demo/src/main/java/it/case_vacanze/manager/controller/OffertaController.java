package it.case_vacanze.manager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.case_vacanze.manager.dto.request.OffertaRequest;
import it.case_vacanze.manager.dto.response.OffertaResponse;
import it.case_vacanze.manager.services.OffertaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/offerte")
public class OffertaController {

    private final OffertaService offertaService;

    public OffertaController(OffertaService offertaService) {
        this.offertaService = offertaService;
    }

    @GetMapping
    public List<OffertaResponse> getAllOfferte() {
        return offertaService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OffertaResponse createOfferta(@Valid @RequestBody OffertaRequest req) {
        return offertaService.crea(req);
    }
}
