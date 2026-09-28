package it.case_vacanze.manager.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.case_vacanze.manager.dto.request.LoginRequest;
import it.case_vacanze.manager.dto.request.RegistrazioneRequest;
import it.case_vacanze.manager.entity.Clienti;
import it.case_vacanze.manager.exception.CredenzialiNonValideException;
import it.case_vacanze.manager.repository.ClientiRepository;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock ClientiRepository clientiRepository;
    @Mock TokenService tokenService;

    // Encoder vero: il test deve verificare la cifratura, non una finta
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private ClienteService service;

    @BeforeEach
    void setUp() {
        service = new ClienteService(clientiRepository, tokenService, passwordEncoder);
    }

    private Clienti clienteConPassword(String password) {
        return new Clienti("Mario", "Rossi", "mario@example.com", passwordEncoder.encode(password), null);
    }

    @Test
    @SuppressWarnings("null") // any() di Mockito restituisce null per costruzione
    void registrazioneSalvaSoloLHash() {
        when(clientiRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.registra(new RegistrazioneRequest("Mario", "Rossi", "mario@example.com", "Password1!"));

        ArgumentCaptor<Clienti> salvato = ArgumentCaptor.forClass(Clienti.class);
        verify(clientiRepository).save(salvato.capture());
        String inDatabase = salvato.getValue().getPassword();
        assertThat(inDatabase).isNotEqualTo("Password1!").startsWith("$2a$");
        assertThat(passwordEncoder.matches("Password1!", inDatabase)).isTrue();
    }

    @Test
    void loginConPasswordGiusta() {
        when(clientiRepository.findByEmail("mario@example.com")).thenReturn(Optional.of(clienteConPassword("Password1!")));
        when(tokenService.creaToken(any())).thenReturn("token");

        assertThat(service.login(new LoginRequest("mario@example.com", "Password1!")).token()).isEqualTo("token");
    }

    @Test
    void loginConPasswordSbagliataDa401() {
        when(clientiRepository.findByEmail("mario@example.com")).thenReturn(Optional.of(clienteConPassword("Password1!")));

        assertThatThrownBy(() -> service.login(new LoginRequest("mario@example.com", "sbagliata")))
                .isInstanceOf(CredenzialiNonValideException.class);
    }

    @Test
    void loginConEmailInesistenteDa401() {
        when(clientiRepository.findByEmail("nessuno@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.login(new LoginRequest("nessuno@example.com", "Password1!")))
                .isInstanceOf(CredenzialiNonValideException.class);
    }
}
