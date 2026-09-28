package it.case_vacanze.manager.config;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.case_vacanze.manager.entity.Clienti;
import it.case_vacanze.manager.repository.ClientiRepository;

// All'avvio cifra le password rimaste in chiaro nel database (utenti creati prima di BCrypt).
// Le password già cifrate vengono riconosciute e lasciate come sono: rieseguirla non fa danni.
@Component
public class MigrazionePassword implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(MigrazionePassword.class);

    // Formato di un hash BCrypt: $2a$10$ seguito da 53 caratteri
    private static final Pattern HASH_BCRYPT = Pattern.compile("^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$");

    private final ClientiRepository clientiRepository;
    private final PasswordEncoder passwordEncoder;

    public MigrazionePassword(ClientiRepository clientiRepository, PasswordEncoder passwordEncoder) {
        this.clientiRepository = clientiRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        int cifrate = 0;
        for (Clienti cliente : clientiRepository.findAll()) {
            if (!eGiaCifrata(cliente.getPassword())) {
                cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
                cifrate++;
            }
        }
        if (cifrate > 0) {
            log.info("Cifrate con BCrypt {} password che erano salvate in chiaro", cifrate);
        }
    }

    static boolean eGiaCifrata(String password) {
        return password != null && HASH_BCRYPT.matcher(password).matches();
    }
}
