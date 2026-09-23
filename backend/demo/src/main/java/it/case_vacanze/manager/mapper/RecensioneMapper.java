package it.case_vacanze.manager.mapper;

import it.case_vacanze.manager.dto.response.RecensioneResponse;
import it.case_vacanze.manager.entity.Recensioni;

public final class RecensioneMapper {

    private RecensioneMapper() {}

    public static RecensioneResponse toResponse(Recensioni recensione) {
        return new RecensioneResponse(recensione.getId(), recensione.getTesto(), recensione.getValutazione(),
                recensione.getCliente_id(),
                recensione.getCliente() != null ? ClienteMapper.toAutore(recensione.getCliente()) : null);
    }
}
