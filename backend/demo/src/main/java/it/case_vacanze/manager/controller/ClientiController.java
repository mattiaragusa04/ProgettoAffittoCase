package it.case_vacanze.manager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.case_vacanze.manager.dto.request.GoogleLoginRequest;
import it.case_vacanze.manager.dto.request.LoginRequest;
import it.case_vacanze.manager.dto.request.RegistrazioneRequest;
import it.case_vacanze.manager.dto.response.ClienteResponse;
import it.case_vacanze.manager.services.ClienteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clienti")
public class ClientiController {

    private final ClienteService clienteService;

    public ClientiController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteResponse> getAllClienti() {
        return clienteService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse registra(@Valid @RequestBody RegistrazioneRequest req) {
        return clienteService.registra(req);
    }

    @PostMapping("/login")
    public ClienteResponse login(@Valid @RequestBody LoginRequest req) {
        return clienteService.login(req);
    }

    @PostMapping("/google")
    public ClienteResponse loginGoogle(@Valid @RequestBody GoogleLoginRequest req) {
        return clienteService.loginGoogle(req);
    }
}
