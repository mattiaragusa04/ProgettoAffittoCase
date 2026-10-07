package it.case_vacanze.manager.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import it.case_vacanze.manager.dto.request.StanzaRequest;
import it.case_vacanze.manager.dto.response.StanzaResponse;
import it.case_vacanze.manager.entity.Stanze;
import it.case_vacanze.manager.exception.ConflittoException;
import it.case_vacanze.manager.exception.RisorsaNonTrovataException;
import it.case_vacanze.manager.repository.OffertaRepository;
import it.case_vacanze.manager.repository.StanzeRepository;

@ExtendWith(MockitoExtension.class)
class StanzaServiceTest {

    @Mock StanzeRepository stanzeRepository;
    @Mock OffertaRepository offertaRepository;
    @InjectMocks StanzaService service;

    private final StanzaRequest richiesta = new StanzaRequest(201, 2, 80.0, "img.png", "Vista mare", "Doppia", null);

    @Test
    void creaSalvaStanzaPrenotabileConValutazioneZero() {
        when(stanzeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        StanzaResponse risposta = service.crea(richiesta);

        ArgumentCaptor<Stanze> salvata = ArgumentCaptor.forClass(Stanze.class);
        verify(stanzeRepository).save(salvata.capture());
        assertThat(salvata.getValue().getId()).isNull();
        assertThat(salvata.getValue().getDisponibile()).isTrue();
        assertThat(salvata.getValue().getValutazione()).isEqualTo(0.0);
        assertThat(risposta.numero()).isEqualTo(201);
        assertThat(risposta.numeroPosti()).isEqualTo(2);
        assertThat(risposta.prezzo()).isEqualTo(80.0);
    }

    @Test
    void numeroGiaUsatoDaConflitto() {
        when(stanzeRepository.existsByNumero(201)).thenReturn(true);

        assertThatThrownBy(() -> service.crea(richiesta))
                .isInstanceOf(ConflittoException.class)
                .hasMessage("Esiste già una stanza con il numero 201");
        verify(stanzeRepository, never()).save(any());
    }

    @Test
    void offertaInesistenteDaNonTrovata() {
        StanzaRequest conOfferta = new StanzaRequest(201, 2, 80.0, "img.png", "Vista mare", "Doppia", 99);
        when(offertaRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> service.crea(conOfferta))
                .isInstanceOf(RisorsaNonTrovataException.class)
                .hasMessage("Offerta non trovata");
        verify(stanzeRepository, never()).save(any());
    }
}
