package it.case_vacanze.manager.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.case_vacanze.manager.dto.request.StanzaRequest;
import it.case_vacanze.manager.dto.response.StanzaResponse;
import it.case_vacanze.manager.entity.Stanze;
import it.case_vacanze.manager.exception.ConflittoException;
import it.case_vacanze.manager.exception.RichiestaNonValidaException;
import it.case_vacanze.manager.exception.RisorsaNonTrovataException;
import it.case_vacanze.manager.mapper.StanzaMapper;
import it.case_vacanze.manager.repository.OffertaRepository;
import it.case_vacanze.manager.repository.PrenotazioneRepository;
import it.case_vacanze.manager.repository.StanzeRepository;

@Service
@Transactional(readOnly = true)
public class StanzaService {

    private final StanzeRepository stanzeRepository;
    private final OffertaRepository offertaRepository;
    private final PrenotazioneRepository prenotazioneRepository;

    public StanzaService(StanzeRepository stanzeRepository, OffertaRepository offertaRepository, PrenotazioneRepository prenotazioneRepository) {
        this.stanzeRepository = stanzeRepository;
        this.offertaRepository = offertaRepository;
        this.prenotazioneRepository = prenotazioneRepository;
    }

    public List<StanzaResponse> findAll() {
        return stanzeRepository.findAll().stream().map(StanzaMapper::toResponse).toList();
    }

    public StanzaResponse trova(int id) {
        return stanzeRepository.findById(id)
                .map(StanzaMapper::toResponse)
                .orElseThrow(() -> new RisorsaNonTrovataException("Stanza non trovata"));
    }

    public List<StanzaResponse> cercaDisponibili(LocalDate checkIn, LocalDate checkOut, Integer ospiti) {
        if (!checkOut.isAfter(checkIn)) {
            throw new RichiestaNonValidaException("Il check-out deve essere successivo al check-in");
        }
        if (ospiti < 1) {
            throw new RichiestaNonValidaException("Serve almeno un ospite");
        }
        return stanzeRepository.findAvailableRooms(checkIn, checkOut, ospiti).stream()
                .map(StanzaMapper::toResponse).toList();
    }

    @Transactional
    public StanzaResponse crea(StanzaRequest req) {
        if (stanzeRepository.existsByNumero(req.numero())) {
            throw new ConflittoException("Esiste già una stanza con il numero " + req.numero());
        }
        if (req.codiceOfferta() != null && !offertaRepository.existsById(req.codiceOfferta())) {
            throw new RisorsaNonTrovataException("Offerta non trovata");
        }
        Stanze stanza = StanzaMapper.toEntity(req);
        return StanzaMapper.toResponse(stanzeRepository.save(stanza));
    }
    @Transactional
    public void cancella(int id) {
        if (prenotazioneRepository.existsByStanzaId(id)) {
            throw new ConflittoException("La stanza è prenotata e non può essere cancellata");
        }
        if (!stanzeRepository.existsById(id)) {
            throw new RisorsaNonTrovataException("Stanza non trovata");
        }
        stanzeRepository.deleteById(id);
    }
}

