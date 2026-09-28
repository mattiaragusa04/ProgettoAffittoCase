package it.case_vacanze.manager.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.case_vacanze.manager.entity.Clienti;
import it.case_vacanze.manager.repository.ClientiRepository;

@ExtendWith(MockitoExtension.class)
class MigrazionePasswordTest {

    @Mock ClientiRepository clientiRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void cifraSoloLePasswordInChiaro() throws Exception {
        String hashEsistente = passwordEncoder.encode("Admin123!");
        Clienti inChiaro = new Clienti("Mario", "Rossi", "mario@example.com", "Password1!", null);
        Clienti giaCifrato = new Clienti("Admin", "Ragusa", "admin@example.com", hashEsistente, null);
        when(clientiRepository.findAll()).thenReturn(List.of(inChiaro, giaCifrato));

        new MigrazionePassword(clientiRepository, passwordEncoder).run(null);

        assertThat(passwordEncoder.matches("Password1!", inChiaro.getPassword())).isTrue();
        assertThat(giaCifrato.getPassword()).isEqualTo(hashEsistente);
    }

    @Test
    void riconosceGliHashBCrypt() {
        assertThat(MigrazionePassword.eGiaCifrata(passwordEncoder.encode("x"))).isTrue();
        assertThat(MigrazionePassword.eGiaCifrata("Password1!")).isFalse();
        assertThat(MigrazionePassword.eGiaCifrata(null)).isFalse();
    }
}
