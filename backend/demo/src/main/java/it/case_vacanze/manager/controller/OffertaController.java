package it.case_vacanze.manager.controller;

import org.springframework.web.bind.annotation.RequestBody ;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import it.case_vacanze.manager.entity.Offerta;
import it.case_vacanze.manager.repository.OffertaRepository;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/offerte")
public class OffertaController {
    @Autowired
    private OffertaRepository offertaRepository;
    @GetMapping
    public List<Offerta> getAllOfferte() { return offertaRepository.findAll(); }

    @PostMapping
    public Offerta createOfferta(@RequestBody Offerta offerta) {
        return offertaRepository.save(offerta);
    }
}
