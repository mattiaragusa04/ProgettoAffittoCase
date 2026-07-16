package it.case_vacanze.manager.controller;
import it.case_vacanze.manager.entity.Clienti;
import it.case_vacanze.manager.repository.ClientiRepository;

import java.util.List;
import java.util.Map;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clienti")
public class ClientiController {

    private final ClientiRepository clientiRepository;

    public ClientiController(ClientiRepository clientiRepository) {
        this.clientiRepository = clientiRepository;
    }

    @GetMapping
    public List<Clienti> getAllClienti() {
        return clientiRepository.findAll();
    }
    
    @PostMapping
    public ResponseEntity<?> createClienti(@RequestBody Clienti clienti) {
        if (clientiRepository.findByEmail(clienti.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email già registrata");
        }
        return ResponseEntity.ok(clientiRepository.save(clienti));
    }

    @PostMapping("/login")
    public ResponseEntity<Clienti> login(@RequestBody Clienti clienti){
        Clienti user = clientiRepository.findByEmailAndPassword(clienti.getEmail(), clienti.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/google")
    public ResponseEntity<Clienti> loginGoogle(@RequestBody Map<String, Object> googleData){
        String email = (String) googleData.get("email");
        Clienti user = clientiRepository.findByEmail(email);
        String picture = (String) googleData.get("picture");
        if (user == null) {

            // Se l'utente non esiste, lo registriamo usando i campi specifici di Google
            String nome = (String) googleData.get("given_name");
            String cognome = (String) googleData.get("family_name");
            user = new Clienti(nome, cognome, email, "GOOGLE_AUTH", picture);
            user = clientiRepository.save(user);
        }else {
            if(picture != null && !picture.equals(user.getPicture())) {
                user.setPicture(picture);
                user = clientiRepository.save(user);
            }
        }
        return ResponseEntity.ok(user);
    }


    

}
