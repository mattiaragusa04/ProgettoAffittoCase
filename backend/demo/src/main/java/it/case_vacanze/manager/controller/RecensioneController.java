package it.case_vacanze.manager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import it.case_vacanze.manager.repository.RecensioneRepository;
import it.case_vacanze.manager.entity.Recensioni;
import java.util.List;

@RestController
@RequestMapping("/recensioni")
public class RecensioneController {

    private final RecensioneRepository recensioneRepository;

    public RecensioneController(RecensioneRepository recensioneRepository) {
        this.recensioneRepository = recensioneRepository;
    }

    @GetMapping
    public List<Recensioni> getAllRecensioni() { return recensioneRepository.findAll(); }

    @PostMapping
    public Recensioni createRecensione(@RequestBody Recensioni recensione) {
        return recensioneRepository.save(recensione);
    }
    

}
