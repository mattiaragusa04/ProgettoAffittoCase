package it.case_vacanze.manager.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.case_vacanze.manager.dto.response.StanzaResponse;
import it.case_vacanze.manager.exception.RichiestaNonValidaException;
import it.case_vacanze.manager.exception.RisorsaNonTrovataException;
import it.case_vacanze.manager.mapper.StanzaMapper;
import it.case_vacanze.manager.repository.StanzeRepository;

@Service
@Transactional(readOnly = true)
public class StanzaService {

    private final StanzeRepository stanzeRepository;

    public StanzaService(StanzeRepository stanzeRepository) {
        this.stanzeRepository = stanzeRepository;
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
}
