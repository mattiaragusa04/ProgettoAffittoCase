package it.case_vacanze.manager.services;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.case_vacanze.manager.dto.request.PrenotazioneRequest;
import it.case_vacanze.manager.dto.response.PrenotazioneResponse;
import it.case_vacanze.manager.entity.Prenotazione;
import it.case_vacanze.manager.entity.Stanze;
import it.case_vacanze.manager.exception.ConflittoException;
import it.case_vacanze.manager.exception.RichiestaNonValidaException;
import it.case_vacanze.manager.exception.RisorsaNonTrovataException;
import it.case_vacanze.manager.mapper.PrenotazioneMapper;
import it.case_vacanze.manager.repository.ClientiRepository;
import it.case_vacanze.manager.repository.PrenotazioneRepository;
import it.case_vacanze.manager.repository.StanzeRepository;

@Service
@Transactional(readOnly = true)
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final StanzeRepository stanzeRepository;
    private final ClientiRepository clientiRepository;

    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository,
                               StanzeRepository stanzeRepository,
                               ClientiRepository clientiRepository) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.stanzeRepository = stanzeRepository;
        this.clientiRepository = clientiRepository;
    }

    public List<PrenotazioneResponse> findAll() {
        return prenotazioneRepository.findAll().stream().map(PrenotazioneMapper::toResponse).toList();
    }

    @Transactional
    public PrenotazioneResponse crea(PrenotazioneRequest req) {
        if (!req.checkOut().isAfter(req.checkIn())) {
            throw new RichiestaNonValidaException("Il check-out deve essere successivo al check-in");
        }
        if (!clientiRepository.existsById(req.clienteId())) {
            throw new RisorsaNonTrovataException("Cliente non trovato");
        }
        Stanze stanza = stanzeRepository.findById(req.stanzaId())
                .orElseThrow(() -> new RisorsaNonTrovataException("Stanza non trovata"));

        if (!Boolean.TRUE.equals(stanza.getDisponibile())) {
            throw new ConflittoException("La stanza non è prenotabile");
        }
        if (req.numeroPersone() > stanza.getNumero_posti()) {
            throw new RichiestaNonValidaException(
                    "La stanza ospita al massimo " + stanza.getNumero_posti() + " persone");
        }
        if (prenotazioneRepository.existsSovrapposizione(stanza.getId(), req.checkIn(), req.checkOut())) {
            throw new ConflittoException("La stanza è già prenotata in queste date");
        }

        long notti = ChronoUnit.DAYS.between(req.checkIn(), req.checkOut());
        double prezzoTotale = notti * stanza.getPrezzo();

        Prenotazione prenotazione = new Prenotazione(req.checkIn(), req.checkOut(), prezzoTotale,
                req.numeroPersone(), req.clienteId(), stanza.getId());
        return PrenotazioneMapper.toResponse(prenotazioneRepository.save(prenotazione));
    }
}
