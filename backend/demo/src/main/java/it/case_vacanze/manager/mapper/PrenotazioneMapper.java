package it.case_vacanze.manager.mapper;

import it.case_vacanze.manager.dto.response.PrenotazioneResponse;
import it.case_vacanze.manager.entity.Prenotazione;

public final class PrenotazioneMapper {

    private PrenotazioneMapper() {}

    public static PrenotazioneResponse toResponse(Prenotazione prenotazione) {
        return new PrenotazioneResponse(prenotazione.getId(), prenotazione.getData_check_in(),
                prenotazione.getData_check_out(), prenotazione.getNumero_persone(),
                prenotazione.getPrezzo_totale(), prenotazione.getCliente_id(), prenotazione.getStanza_id());
    }
}
