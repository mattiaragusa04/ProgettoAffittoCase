package it.case_vacanze.manager.controller;

import it.case_vacanze.manager.entity.Newsletter;
import it.case_vacanze.manager.repository.NewsletterRepository;
import it.case_vacanze.manager.services.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/newsletter")
@CrossOrigin(origins = "*")
public class NewsletterController {

    @Autowired
    private NewsletterRepository newsletterRepository;
    @Autowired
    private EmailService emailService;
    

    @GetMapping
    public List<Newsletter> getAllNewsletter() {
        return newsletterRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> iscriviti(@RequestBody Newsletter newsletter) {
        if (newsletterRepository.findByEmail(newsletter.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email già iscritta alla newsletter");
        }
        Newsletter newsletterSalvata = newsletterRepository.save(newsletter);

        // Invia email di benvenuto
        try{
            String oggetto = "Benvenuto alla Newsletter di Case Vacanze!";
            String testo = "Grazie per esserti iscritto alla nostra newsletter! Riceverai aggiornamenti su offerte e novità.";
            emailService.inviaEmail(newsletter.getEmail(), oggetto, testo);
        } catch (Exception e) {
            // Log dell'errore (puoi usare un logger come Log4j o SLF4J)
            System.err.println("Errore durante l'invio dell'email: " + e.getMessage());
        }

        return ResponseEntity.ok(newsletterSalvata);
    }
}
