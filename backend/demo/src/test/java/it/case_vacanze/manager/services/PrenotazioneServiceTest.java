package it.case_vacanze.manager.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import it.case_vacanze.manager.dto.request.PrenotazioneRequest;
import it.case_vacanze.manager.dto.response.PrenotazioneResponse;
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
        when(clientiRepository.existsById(1)).thenReturn(true);
    }

    private PrenotazioneRequest richiesta(int persone) {
        return new PrenotazioneRequest(CHECK_IN, CHECK_OUT, persone, 1, 10);
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
}
