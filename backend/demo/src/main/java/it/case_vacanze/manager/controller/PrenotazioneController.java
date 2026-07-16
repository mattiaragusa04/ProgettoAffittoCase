package it.case_vacanze.manager.controller;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import it.case_vacanze.manager.entity.Prenotazione;
import it.case_vacanze.manager.repository.PrenotazioneRepository;
import org.springframework.web.bind.annotation.RequestBody;
@RestController
@RequestMapping("/prenotazione")
public class PrenotazioneController {
    
    private final PrenotazioneRepository prenotazioneRepository;

    public PrenotazioneController(PrenotazioneRepository prenotazioneRepository) {
        this.prenotazioneRepository = prenotazioneRepository;
    }

    @GetMapping
    public List<Prenotazione> getAllPrenotazioni() { return prenotazioneRepository.findAll(); }

    @PostMapping
    public Prenotazione createPrenotazione(@RequestBody Prenotazione prenotazione) {
        return prenotazioneRepository.save(prenotazione);
    }
}
