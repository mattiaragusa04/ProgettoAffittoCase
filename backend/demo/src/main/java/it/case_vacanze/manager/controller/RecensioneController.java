package it.case_vacanze.manager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.case_vacanze.manager.dto.request.RecensioneRequest;
import it.case_vacanze.manager.dto.response.RecensioneResponse;
import it.case_vacanze.manager.services.RecensioneService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/recensioni")
public class RecensioneController {

    private final RecensioneService recensioneService;

    public RecensioneController(RecensioneService recensioneService) {
        this.recensioneService = recensioneService;
    }

    @GetMapping
    public List<RecensioneResponse> getAllRecensioni() {
        return recensioneService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecensioneResponse createRecensione(@Valid @RequestBody RecensioneRequest req) {
        return recensioneService.crea(req);
    }
}
