package it.case_vacanze.manager.mapper;

import org.springframework.lang.NonNull;

import it.case_vacanze.manager.dto.request.StanzaRequest;
import it.case_vacanze.manager.dto.response.StanzaResponse;
import it.case_vacanze.manager.entity.Stanze;

public final class StanzaMapper {

    private StanzaMapper() {}

    // Nuova stanza: id generato dal database, prenotabile, valutazione di partenza 0
    @NonNull
    public static Stanze toEntity(StanzaRequest req) {
        return new Stanze(null, req.numero(), req.numeroPosti(), true, req.prezzo(), req.immagine(),
                req.descrizione(), req.tipo(), 0.0, req.codiceOfferta());
    }

    public static StanzaResponse toResponse(Stanze stanza) {
        return new StanzaResponse(stanza.getId(), stanza.getNumero(), stanza.getNumero_posti(),
                stanza.getDisponibile(), stanza.getPrezzo(), stanza.getImmagine(), stanza.getDescrizione(),
                stanza.getTipo(), stanza.getValutazione(), stanza.getCodice_off());
    }
}
