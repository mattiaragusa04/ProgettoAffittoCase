package it.case_vacanze.manager.mapper;

import org.springframework.lang.NonNull;

import it.case_vacanze.manager.dto.request.RegistrazioneRequest;
import it.case_vacanze.manager.dto.response.AutoreResponse;
import it.case_vacanze.manager.dto.response.ClienteResponse;
import it.case_vacanze.manager.entity.Clienti;

public final class ClienteMapper {

    private ClienteMapper() {}

    @NonNull
    public static Clienti toEntity(RegistrazioneRequest req) {
        return new Clienti(req.nome(), req.cognome(), req.email(), req.password(), null);
    }

    public static ClienteResponse toResponse(Clienti cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getCognome(),
                cliente.getEmail(), cliente.getPicture());
    }

    public static AutoreResponse toAutore(Clienti cliente) {
        return new AutoreResponse(cliente.getId(), cliente.getNome(), cliente.getCognome());
    }
}
