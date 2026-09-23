package it.case_vacanze.manager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import it.case_vacanze.manager.config.CorsConfig;
import it.case_vacanze.manager.dto.response.AutoreResponse;
import it.case_vacanze.manager.dto.response.ClienteResponse;
import it.case_vacanze.manager.dto.response.OffertaResponse;
import it.case_vacanze.manager.dto.response.RecensioneResponse;
import it.case_vacanze.manager.exception.ConflittoException;
import it.case_vacanze.manager.exception.CredenzialiNonValideException;
import it.case_vacanze.manager.services.ClienteService;
import it.case_vacanze.manager.services.NewsletterService;
import it.case_vacanze.manager.services.OffertaService;
import it.case_vacanze.manager.services.PrenotazioneService;
import it.case_vacanze.manager.services.RecensioneService;
import it.case_vacanze.manager.services.StanzaService;

// Verifica che il JSON resti quello letto dal frontend React e che gli errori abbiano lo status giusto
@WebMvcTest
@Import(CorsConfig.class)
class ApiContractTest {

    @Autowired MockMvc mvc;

    @MockBean ClienteService clienteService;
    @MockBean NewsletterService newsletterService;
    @MockBean OffertaService offertaService;
    @MockBean PrenotazioneService prenotazioneService;
    @MockBean RecensioneService recensioneService;
    @MockBean StanzaService stanzaService;

    @Test
    void offerteUsanoICampiSnakeCase() throws Exception {
        when(offertaService.findAll()).thenReturn(List.of(
                new OffertaResponse(1, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 31), 79.0, "img.png")));

        mvc.perform(get("/offerte"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].data_fine").value("2026-07-31"))
                .andExpect(jsonPath("$[0].prezzo_scontato").value(79.0))
                .andExpect(jsonPath("$[0].immagine_off").value("img.png"));
    }

    @Test
    void recensioniContengonoAutoreSenzaEmailNePassword() throws Exception {
        when(recensioneService.findAll()).thenReturn(List.of(
                new RecensioneResponse(1, "Ottimo", 5, 3, new AutoreResponse(3, "Mario", "Rossi"))));

        mvc.perform(get("/recensioni"))
                .andExpect(jsonPath("$[0].cliente.nome").value("Mario"))
                .andExpect(jsonPath("$[0].cliente.email").doesNotExist())
                .andExpect(jsonPath("$[0].cliente.password").doesNotExist());
    }

    @Test
    void recensioneConValutazioneFuoriScalaDa400() throws Exception {
        mvc.perform(post("/recensioni").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"testo\":\"ok\",\"valutazione\":50,\"cliente_id\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dettagli.valutazione").value("La valutazione massima è 5"));
    }

    @Test
    void recensioneValidaDa201() throws Exception {
        when(recensioneService.crea(any())).thenReturn(new RecensioneResponse(1, "ok", 4, 1, null));

        mvc.perform(post("/recensioni").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"testo\":\"ok\",\"valutazione\":4,\"cliente_id\":1}"))
                .andExpect(status().isCreated());
    }

    @Test
    void loginNonRestituisceLaPassword() throws Exception {
        when(clienteService.login(any())).thenReturn(new ClienteResponse(1, "Mario", "Rossi", "m@r.it", null));

        mvc.perform(post("/clienti/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"m@r.it\",\"password\":\"x!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Mario"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void loginErratoDa401() throws Exception {
        when(clienteService.login(any())).thenThrow(new CredenzialiNonValideException("Email o password errati"));

        mvc.perform(post("/clienti/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"m@r.it\",\"password\":\"sbagliata\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.messaggio").value("Email o password errati"));
    }

    @Test
    void emailGiaIscrittaDa409() throws Exception {
        when(newsletterService.iscrivi(any())).thenThrow(new ConflittoException("Email già iscritta alla newsletter"));

        mvc.perform(post("/newsletter").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"a@b.it\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void ricercaStanzeConDataNonValidaDa400() throws Exception {
        mvc.perform(get("/stanze/search").param("checkIn", "ciao").param("checkOut", "2026-07-03").param("guests", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void corsConsenteIlFrontendVite() throws Exception {
        mvc.perform(options("/offerte")
                        .header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
    }

    @Test
    void corsConsenteViteSuUnaltraPorta() throws Exception {
        mvc.perform(options("/clienti/login")
                        .header("Origin", "http://localhost:5174")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5174"));
    }

    @Test
    void corsBloccaAltriSiti() throws Exception {
        mvc.perform(options("/clienti/login")
                        .header("Origin", "https://sito-malevolo.example")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isForbidden());
    }
}
