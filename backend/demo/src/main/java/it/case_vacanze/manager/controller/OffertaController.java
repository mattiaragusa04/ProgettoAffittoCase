package it.case_vacanze.manager.controller;

import org.springframework.web.bind.annotation.RequestBody ;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import it.case_vacanze.manager.entity.Offerta;
import it.case_vacanze.manager.repository.OffertaRepository;
import java.util.List;

@RestController
@RequestMapping("/offerte")
public class OffertaController {
    private final OffertaRepository offertaRepository;

    public OffertaController(OffertaRepository offertaRepository) {
        this.offertaRepository = offertaRepository;
    }

    @GetMapping
    public List<Offerta> getAllOfferte() { return offertaRepository.findAll(); }

    @PostMapping
    public Offerta createOfferta(@RequestBody Offerta offerta) {
        return offertaRepository.save(offerta);
    }
}
