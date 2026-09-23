package it.case_vacanze.manager.mapper;

import it.case_vacanze.manager.dto.response.StanzaResponse;
import it.case_vacanze.manager.entity.Stanze;

public final class StanzaMapper {

    private StanzaMapper() {}

    public static StanzaResponse toResponse(Stanze stanza) {
        return new StanzaResponse(stanza.getId(), stanza.getNumero(), stanza.getNumero_posti(),
                stanza.getDisponibile(), stanza.getPrezzo(), stanza.getImmagine(), stanza.getDescrizione(),
                stanza.getTipo(), stanza.getValutazione(), stanza.getCodice_off());
    }
}
