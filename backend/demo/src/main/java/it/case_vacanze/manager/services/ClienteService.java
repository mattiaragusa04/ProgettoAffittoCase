package it.case_vacanze.manager.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.case_vacanze.manager.dto.request.GoogleLoginRequest;
import it.case_vacanze.manager.dto.request.LoginRequest;
import it.case_vacanze.manager.dto.request.RegistrazioneRequest;
import it.case_vacanze.manager.dto.response.ClienteResponse;
import it.case_vacanze.manager.entity.Clienti;
import it.case_vacanze.manager.exception.ConflittoException;
import it.case_vacanze.manager.exception.CredenzialiNonValideException;
import it.case_vacanze.manager.mapper.ClienteMapper;
import it.case_vacanze.manager.repository.ClientiRepository;

@Service
@Transactional(readOnly = true)
public class ClienteService {

    // Password segnaposto dei clienti registrati con Google: non vale per il login con email e password
    private static final String GOOGLE_AUTH = "GOOGLE_AUTH";

    private final ClientiRepository clientiRepository;

    public ClienteService(ClientiRepository clientiRepository) {
        this.clientiRepository = clientiRepository;
    }

    public List<ClienteResponse> findAll() {
        return clientiRepository.findAll().stream().map(ClienteMapper::toResponse).toList();
    }

    @Transactional
    public ClienteResponse registra(RegistrazioneRequest req) {
        if (clientiRepository.existsByEmail(req.email())) {
            throw new ConflittoException("Email già registrata");
        }
        return ClienteMapper.toResponse(clientiRepository.save(ClienteMapper.toEntity(req)));
    }

    public ClienteResponse login(LoginRequest req) {
        if (GOOGLE_AUTH.equals(req.password())) {
            throw new CredenzialiNonValideException("Email o password errati");
        }
        return clientiRepository.findByEmailAndPassword(req.email(), req.password())
                .map(ClienteMapper::toResponse)
                .orElseThrow(() -> new CredenzialiNonValideException("Email o password errati"));
    }

    // Se il cliente non esiste lo registra con i dati di Google, altrimenti aggiorna la foto profilo
    @Transactional
    public ClienteResponse loginGoogle(GoogleLoginRequest req) {
        Clienti cliente = clientiRepository.findByEmail(req.email()).orElse(null);
        if (cliente == null) {
            cliente = clientiRepository.save(
                    new Clienti(req.nome(), req.cognome(), req.email(), GOOGLE_AUTH, req.picture()));
        } else if (req.picture() != null && !req.picture().equals(cliente.getPicture())) {
            cliente.setPicture(req.picture());
        }
        return ClienteMapper.toResponse(cliente);
    }
}
