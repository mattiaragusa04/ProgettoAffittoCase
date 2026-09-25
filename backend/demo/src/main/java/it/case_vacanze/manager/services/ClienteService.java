package it.case_vacanze.manager.services;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.case_vacanze.manager.dto.request.LoginRequest;
import it.case_vacanze.manager.dto.request.ModificaProfiloRequest;
import it.case_vacanze.manager.dto.request.RegistrazioneRequest;
import it.case_vacanze.manager.dto.response.AuthResponse;
import it.case_vacanze.manager.dto.response.ClienteResponse;
import it.case_vacanze.manager.entity.Clienti;
import it.case_vacanze.manager.exception.ConflittoException;
import it.case_vacanze.manager.exception.CredenzialiNonValideException;
import it.case_vacanze.manager.exception.RisorsaNonTrovataException;
import it.case_vacanze.manager.mapper.ClienteMapper;
import it.case_vacanze.manager.repository.ClientiRepository;

@Service
@Transactional(readOnly = true)
public class ClienteService {

    private final ClientiRepository clientiRepository;
    private final TokenService tokenService;

    public ClienteService(ClientiRepository clientiRepository, TokenService tokenService) {
        this.clientiRepository = clientiRepository;
        this.tokenService = tokenService;
    }

    public List<ClienteResponse> findAll() {
        return clientiRepository.findAll().stream().map(ClienteMapper::toResponse).toList();
    }

    // Dati dell'utente loggato
    public ClienteResponse getDatiUtente() {
        return ClienteMapper.toResponse(clienteLoggato());
    }

    // Aggiorna nome e cognome dell'utente loggato.
    // Nessun save(): dentro la transazione Hibernate salva da solo le modifiche all'entity.
    @Transactional
    public ClienteResponse modificaDatiUtente(ModificaProfiloRequest req) {
        Clienti cliente = clienteLoggato();
        cliente.setNome(req.nome().trim());
        cliente.setCognome(req.cognome().trim());
        return ClienteMapper.toResponse(cliente);
    }

    @Transactional
    public AuthResponse registra(RegistrazioneRequest req) {
        if (clientiRepository.existsByEmail(req.email())) {
            throw new ConflittoException("Email già registrata");
        }
        return autentica(clientiRepository.save(ClienteMapper.toEntity(req)));
    }

    public AuthResponse login(LoginRequest req) {
        return clientiRepository.findByEmailAndPassword(req.email(), req.password())
                .map(this::autentica)
                .orElseThrow(() -> new CredenzialiNonValideException("Email o password errati"));
    }

    // L'utente loggato: l'email arriva dal token JWT, non dal client
    private Clienti clienteLoggato() {
        var autenticazione = SecurityContextHolder.getContext().getAuthentication();
        if (autenticazione == null) {
            throw new CredenzialiNonValideException("Utente non autenticato");
        }
        return clientiRepository.findByEmail(autenticazione.getName())
                .orElseThrow(() -> new RisorsaNonTrovataException("Utente non trovato"));
    }

    private AuthResponse autentica(Clienti cliente) {
        return new AuthResponse(tokenService.creaToken(cliente), ClienteMapper.toResponse(cliente));
    }
}
