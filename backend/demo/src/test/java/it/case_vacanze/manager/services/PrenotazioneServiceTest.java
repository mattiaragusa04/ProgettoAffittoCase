package it.case_vacanze.manager.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import it.case_vacanze.manager.dto.request.PrenotazioneRequest;
import it.case_vacanze.manager.dto.response.PrenotazioneResponse;
import it.case_vacanze.manager.entity.Clienti;
import it.case_vacanze.manager.entity.Prenotazione;
import it.case_vacanze.manager.entity.Stanze;
import it.case_vacanze.manager.exception.ConflittoException;
import it.case_vacanze.manager.exception.RichiestaNonValidaException;
import it.case_vacanze.manager.exception.RisorsaNonTrovataException;
import it.case_vacanze.manager.repository.ClientiRepository;
import it.case_vacanze.manager.repository.PrenotazioneRepository;
import it.case_vacanze.manager.repository.StanzeRepository;

@ExtendWith(MockitoExtension.class)
class PrenotazioneServiceTest {

    private static final LocalDate CHECK_IN = LocalDate.of(2026, 7, 1);
    private static final LocalDate CHECK_OUT = LocalDate.of(2026, 7, 4);

    @Mock PrenotazioneRepository prenotazioneRepository;
    @Mock StanzeRepository stanzeRepository;
    @Mock ClientiRepository clientiRepository;
    @InjectMocks PrenotazioneService service;

    // Stanza 10: 2 posti, 50 euro a notte, disponibile
    private final Stanze stanza = new Stanze(10, 101, 2, true, 50.0, "img", "desc", "Doppia", 4.5, null);

    @BeforeEach
    void setUp() {
        // Di default è loggato Mario (id 1). lenient: non tutti i test passano dal suo login
        loggatoCome("mario@example.com");
        Clienti mario = mock(Clienti.class);
        lenient().when(mario.getId()).thenReturn(1);
        lenient().when(clientiRepository.findByEmail("mario@example.com")).thenReturn(Optional.of(mario));
    }

    @AfterEach
    void pulisciLogin() {
        SecurityContextHolder.clearContext();
    }

    private void loggatoCome(String email) {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(email, null));
    }

    private PrenotazioneRequest richiesta(int persone) {
        return new PrenotazioneRequest(CHECK_IN, CHECK_OUT, persone, 10);
    }

    @Test
    @SuppressWarnings("null") // any() di Mockito restituisce null per costruzione
    void calcolaIlPrezzoDalleNotti() {
        when(stanzeRepository.findById(10)).thenReturn(Optional.of(stanza));
        when(prenotazioneRepository.save(any())).thenAnswer(inv -> inv.<Prenotazione>getArgument(0));

        PrenotazioneResponse res = service.crea(richiesta(2));

        assertThat(res.prezzoTotale()).isEqualTo(150.0); // 3 notti x 50
        assertThat(res.numeroPersone()).isEqualTo(2);
    }

    @Test
    @SuppressWarnings("null") // any() di Mockito restituisce null per costruzione
    void prenotaComeUtenteDelTokenNonComeAltri() {
        loggatoCome("luigi@example.com");
        Clienti luigi = mock(Clienti.class);
        when(luigi.getId()).thenReturn(2);
        when(clientiRepository.findByEmail("luigi@example.com")).thenReturn(Optional.of(luigi));
        when(stanzeRepository.findById(10)).thenReturn(Optional.of(stanza));
        when(prenotazioneRepository.save(any())).thenAnswer(inv -> inv.<Prenotazione>getArgument(0));

        PrenotazioneResponse res = service.crea(richiesta(2));

        assertThat(res.clienteId()).isEqualTo(2);
    }

    @Test
    void utenteDelTokenInesistenteDa404() {
        loggatoCome("cancellato@example.com");
        when(clientiRepository.findByEmail("cancellato@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.crea(richiesta(2))).isInstanceOf(RisorsaNonTrovataException.class);
    }

    @Test
    void rifiutaTroppePersone() {
        when(stanzeRepository.findById(10)).thenReturn(Optional.of(stanza));

        assertThatThrownBy(() -> service.crea(richiesta(3))).isInstanceOf(RichiestaNonValidaException.class);
    }

    @Test
    void rifiutaDateSovrapposte() {
        when(stanzeRepository.findById(10)).thenReturn(Optional.of(stanza));
        when(prenotazioneRepository.existsSovrapposizione(10, CHECK_IN, CHECK_OUT)).thenReturn(true);

        assertThatThrownBy(() -> service.crea(richiesta(2))).isInstanceOf(ConflittoException.class);
    }

    @Test
    void stanzaInesistenteDa404() {
        when(stanzeRepository.findById(10)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.crea(richiesta(2))).isInstanceOf(RisorsaNonTrovataException.class);
    }

    @Test
    void mieCercaPerIlClienteDelToken() {
        loggatoCome("mario@example.com");
        Clienti mario = mock(Clienti.class);
        when(mario.getId()).thenReturn(1);
        when(clientiRepository.findByEmail("mario@example.com")).thenReturn(Optional.of(mario));
        when(prenotazioneRepository.findByClienteId(1)).thenReturn(List.of(
                new Prenotazione(CHECK_IN, CHECK_OUT, 150.0, 2, 1, 10)));

        List<PrenotazioneResponse> mie = service.findMie();

        assertThat(mie).hasSize(1);
        assertThat(mie.get(0).clienteId()).isEqualTo(1);
    }

    @Test
    void mieSenzaPrenotazioniRestituisceListaVuota() {
        loggatoCome("nuovo@example.com");
        Clienti nuovo = mock(Clienti.class);
        when(nuovo.getId()).thenReturn(7);
        when(clientiRepository.findByEmail("nuovo@example.com")).thenReturn(Optional.of(nuovo));
        when(prenotazioneRepository.findByClienteId(7)).thenReturn(List.of());

        assertThat(service.findMie()).isEmpty();
    }
}
