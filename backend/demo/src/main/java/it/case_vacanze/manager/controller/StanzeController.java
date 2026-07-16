package it.case_vacanze.manager.controller;

import it.case_vacanze.manager.entity.Stanze;
import it.case_vacanze.manager.repository.StanzeRepository;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/stanze")
public class StanzeController {

    private final StanzeRepository stanzeRepository;

    public StanzeController(StanzeRepository stanzeRepository) {
        this.stanzeRepository = stanzeRepository;
    }

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