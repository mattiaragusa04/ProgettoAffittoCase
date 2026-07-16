package it.case_vacanze.manager.controller;

import it.case_vacanze.manager.entity.Stanze;
import it.case_vacanze.manager.repository.StanzeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/stanze")
@CrossOrigin(origins = "*")
public class StanzeController {

    @Autowired
    private StanzeRepository stanzeRepository;

    @GetMapping
    public List<Stanze> getAllStanze() {
        return stanzeRepository.findAll();
    }

    @GetMapping("/search")
    public List<Stanze> searchStanze(
            @RequestParam("checkIn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam("checkOut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam("guests") Integer guests) {
        
        return stanzeRepository.findAvailableRooms(checkIn, checkOut, guests);
    }
}