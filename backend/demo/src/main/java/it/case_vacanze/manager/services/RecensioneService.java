package it.case_vacanze.manager.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.case_vacanze.manager.dto.request.RecensioneRequest;
import it.case_vacanze.manager.dto.response.RecensioneResponse;
import it.case_vacanze.manager.entity.Clienti;
import it.case_vacanze.manager.entity.Recensioni;
import it.case_vacanze.manager.exception.RisorsaNonTrovataException;
import it.case_vacanze.manager.mapper.RecensioneMapper;
import it.case_vacanze.manager.repository.ClientiRepository;
import it.case_vacanze.manager.repository.RecensioneRepository;

@Service
@Transactional(readOnly = true)
public class RecensioneService {

    private final RecensioneRepository recensioneRepository;
    private final ClientiRepository clientiRepository;

    public RecensioneService(RecensioneRepository recensioneRepository, ClientiRepository clientiRepository) {
        this.recensioneRepository = recensioneRepository;
        this.clientiRepository = clientiRepository;
    }

    public List<RecensioneResponse> findAll() {
        return recensioneRepository.findAll().stream().map(RecensioneMapper::toResponse).toList();
    }

    @Transactional
    public RecensioneResponse crea(RecensioneRequest req) {
        Clienti cliente = clientiRepository.findById(req.clienteId())
                .orElseThrow(() -> new RisorsaNonTrovataException("Cliente non trovato"));
        Recensioni recensione = new Recensioni(req.testo(), req.valutazione(), cliente);
        return RecensioneMapper.toResponse(recensioneRepository.save(recensione));
    }
}
