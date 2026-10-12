package it.case_vacanze.manager.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import it.case_vacanze.manager.dto.request.RecensioneRequest;
import it.case_vacanze.manager.dto.response.RecensioneResponse;
import it.case_vacanze.manager.entity.Clienti;
import it.case_vacanze.manager.entity.Recensioni;
import it.case_vacanze.manager.exception.RisorsaNonTrovataException;
import it.case_vacanze.manager.repository.ClientiRepository;
import it.case_vacanze.manager.repository.RecensioneRepository;

@ExtendWith(MockitoExtension.class)
class RecensioneServiceTest {

    @Mock RecensioneRepository recensioneRepository;
    @Mock ClientiRepository clientiRepository;
    @InjectMocks RecensioneService service;

    @AfterEach
    void pulisciLogin() {
        SecurityContextHolder.clearContext();
    }

    private void loggatoCome(String email) {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(email, null));
    }

    @Test
    @SuppressWarnings("null") // any() di Mockito restituisce null per costruzione
    void autoreEUtenteDelToken() {
        loggatoCome("luigi@example.com");
        Clienti luigi = mock(Clienti.class);
        when(luigi.getId()).thenReturn(2);
        when(clientiRepository.findByEmail("luigi@example.com")).thenReturn(Optional.of(luigi));
        when(recensioneRepository.save(any())).thenAnswer(inv -> inv.<Recensioni>getArgument(0));

        RecensioneResponse res = service.crea(new RecensioneRequest("Ottimo soggiorno", 5));

        assertThat(res.clienteId()).isEqualTo(2);
    }

    @Test
    void utenteDelTokenInesistenteDa404() {
        loggatoCome("cancellato@example.com");
        when(clientiRepository.findByEmail("cancellato@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.crea(new RecensioneRequest("ok", 4)))
                .isInstanceOf(RisorsaNonTrovataException.class);
    }
}
