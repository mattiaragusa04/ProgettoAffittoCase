package it.case_vacanze.manager.mapper;

import org.springframework.lang.NonNull;

import it.case_vacanze.manager.dto.request.OffertaRequest;
import it.case_vacanze.manager.dto.response.OffertaResponse;
import it.case_vacanze.manager.entity.Offerta;

public final class OffertaMapper {

    private OffertaMapper() {}

    @NonNull
    public static Offerta toEntity(OffertaRequest req) {
        return new Offerta(req.dataInizio(), req.dataFine(), req.prezzoScontato(), req.immagine());
    }

    public static OffertaResponse toResponse(Offerta offerta) {
        return new OffertaResponse(offerta.getId(), offerta.getData_inizio(), offerta.getData_fine(),
                offerta.getPrezzo_scontato(), offerta.getImmagine_off());
    }
}
