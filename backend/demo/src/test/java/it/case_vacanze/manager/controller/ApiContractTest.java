package it.case_vacanze.manager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.lang.NonNull;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;

import it.case_vacanze.manager.config.CorsConfig;
import it.case_vacanze.manager.config.SecurityConfig;
import it.case_vacanze.manager.dto.response.AuthResponse;
import it.case_vacanze.manager.dto.response.AutoreResponse;
import it.case_vacanze.manager.dto.response.ClienteResponse;
import it.case_vacanze.manager.dto.response.OffertaResponse;
import it.case_vacanze.manager.dto.response.PrenotazioneResponse;
import it.case_vacanze.manager.dto.response.RecensioneResponse;
import it.case_vacanze.manager.entity.Ruolo;
import it.case_vacanze.manager.exception.ConflittoException;
import it.case_vacanze.manager.exception.CredenzialiNonValideException;
import it.case_vacanze.manager.exception.RisorsaNonTrovataException;
import it.case_vacanze.manager.services.ClienteService;
import it.case_vacanze.manager.services.NewsletterService;
import it.case_vacanze.manager.services.OffertaService;
import it.case_vacanze.manager.services.PrenotazioneService;
import it.case_vacanze.manager.services.RecensioneService;
import it.case_vacanze.manager.services.StanzaService;

// Verifica che il JSON resti quello letto dal frontend React e che gli errori abbiano lo status giusto
@WebMvcTest
@Import({CorsConfig.class, SecurityConfig.class})
class ApiContractTest {

    @Autowired MockMvc mvc;

    @MockBean ClienteService clienteService;
    @MockBean NewsletterService newsletterService;
    @MockBean OffertaService offertaService;
    @MockBean PrenotazioneService prenotazioneService;
    @MockBean RecensioneService recensioneService;
    @MockBean StanzaService stanzaService;

    // jwt() di spring-security-test non ha annotazioni di nullità: lo adattiamo qui una volta sola
    @SuppressWarnings("null")
    private static @NonNull RequestPostProcessor conToken(JwtRequestPostProcessor token) {
        return token;
    }

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
        mvc.perform(post("/recensioni").with(conToken(jwt())).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"testo\":\"ok\",\"valutazione\":50,\"cliente_id\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dettagli.valutazione").value("La valutazione massima è 5"));
    }

    @Test
    void recensioneValidaDa201() throws Exception {
        when(recensioneService.crea(any())).thenReturn(new RecensioneResponse(1, "ok", 4, 1, null));

        mvc.perform(post("/recensioni").with(conToken(jwt())).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"testo\":\"ok\",\"valutazione\":4,\"cliente_id\":1}"))
                .andExpect(status().isCreated());
    }

    @Test
    void recensioneSenzaLoginDa401() throws Exception {
        mvc.perform(post("/recensioni").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"testo\":\"ok\",\"valutazione\":4,\"cliente_id\":1}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.messaggio").value("Devi effettuare il login"));
    }

    @Test
    void loginNonRestituisceLaPassword() throws Exception {
        when(clienteService.login(any())).thenReturn(new AuthResponse("token-di-prova",
                new ClienteResponse(1, "Mario", "Rossi", "m@r.it", null, Ruolo.CLIENTE)));

        mvc.perform(post("/clienti/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"m@r.it\",\"password\":\"x!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-di-prova"))
                .andExpect(jsonPath("$.utente.nome").value("Mario"))
                .andExpect(jsonPath("$.utente.ruolo").value("CLIENTE"))
                .andExpect(jsonPath("$.utente.password").doesNotExist());
    }

    @Test
    void datiUtenteConTokenSenzaPassword() throws Exception {
        when(clienteService.getDatiUtente()).thenReturn(new ClienteResponse(1, "Mario", "Rossi", "m@r.it", null, Ruolo.CLIENTE));

        mvc.perform(get("/clienti/me").with(conToken(jwt().jwt(t -> t.subject("m@r.it")))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("m@r.it"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void datiUtenteSenzaTokenDa401() throws Exception {
        mvc.perform(get("/clienti/me")).andExpect(status().isUnauthorized());
    }

    @Test
    void datiUtenteCancellatoDa404() throws Exception {
        when(clienteService.getDatiUtente()).thenThrow(new RisorsaNonTrovataException("Utente non trovato"));

        mvc.perform(get("/clienti/me").with(conToken(jwt()))).andExpect(status().isNotFound());
    }

    @Test
    void modificaDatiUtente() throws Exception {
        when(clienteService.modificaDatiUtente(any())).thenReturn(new ClienteResponse(1, "Marco", "Rossi", "m@r.it", null, Ruolo.CLIENTE));

        mvc.perform(put("/clienti/me").with(conToken(jwt())).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Marco\",\"cognome\":\"Rossi\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Marco"));
    }

    @Test
    void modificaConNomeVuotoDa400() throws Exception {
        mvc.perform(put("/clienti/me").with(conToken(jwt())).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\" \",\"cognome\":\"Rossi\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dettagli.nome").value("Il nome è obbligatorio"));
    }

    @Test
    void modificaSenzaLoginDa401() throws Exception {
        mvc.perform(put("/clienti/me").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Marco\",\"cognome\":\"Rossi\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void miePrenotazioniConToken() throws Exception {
        when(prenotazioneService.findMie()).thenReturn(List.of(new PrenotazioneResponse(
                1, LocalDate.of(2026, 10, 5), LocalDate.of(2026, 10, 8), 2, 540.0, 3, 6)));

        mvc.perform(get("/prenotazione/mie").with(conToken(jwt())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].data_check_in").value("2026-10-05"))
                .andExpect(jsonPath("$[0].prezzo_totale").value(540.0));
    }

    @Test
    void miePrenotazioniSenzaLoginDa401() throws Exception {
        mvc.perform(get("/prenotazione/mie")).andExpect(status().isUnauthorized());
    }

    @Test
    void tutteLePrenotazioniSoloAdmin() throws Exception {
        mvc.perform(get("/prenotazione").with(conToken(jwt().jwt(t -> t.claim("ruolo", "CLIENTE")).authorities(SecurityConfig.ruoliDalToken()))))
                .andExpect(status().isForbidden());
    }

    @Test
    void elencoClientiVietatoAiClienti() throws Exception {
        mvc.perform(get("/clienti").with(conToken(jwt().jwt(t -> t.claim("ruolo", "CLIENTE")).authorities(SecurityConfig.ruoliDalToken()))))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.messaggio").value("Non hai i permessi per questa operazione"));
    }

    @Test
    void elencoClientiConsentitoAllAdmin() throws Exception {
        mvc.perform(get("/clienti").with(conToken(jwt().jwt(t -> t.claim("ruolo", "ADMIN")).authorities(SecurityConfig.ruoliDalToken()))))
                .andExpect(status().isOk());
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
    void stanzaInesistenteDa404() throws Exception {
        when(stanzaService.trova(99)).thenThrow(new RisorsaNonTrovataException("Stanza non trovata"));

        mvc.perform(get("/stanze/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messaggio").value("Stanza non trovata"));
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
