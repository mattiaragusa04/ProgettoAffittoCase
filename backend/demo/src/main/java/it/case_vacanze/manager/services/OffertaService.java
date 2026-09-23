package it.case_vacanze.manager.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.case_vacanze.manager.dto.request.OffertaRequest;
import it.case_vacanze.manager.dto.response.OffertaResponse;
import it.case_vacanze.manager.exception.RichiestaNonValidaException;
import it.case_vacanze.manager.mapper.OffertaMapper;
import it.case_vacanze.manager.repository.OffertaRepository;

@Service
@Transactional(readOnly = true)
public class OffertaService {

    private final OffertaRepository offertaRepository;

    public OffertaService(OffertaRepository offertaRepository) {
        this.offertaRepository = offertaRepository;
    }

    public List<OffertaResponse> findAll() {
        return offertaRepository.findAll().stream().map(OffertaMapper::toResponse).toList();
    }

    @Transactional
    public OffertaResponse crea(OffertaRequest req) {
        if (req.dataFine().isBefore(req.dataInizio())) {
            throw new RichiestaNonValidaException("La data di fine non può precedere la data di inizio");
        }
        return OffertaMapper.toResponse(offertaRepository.save(OffertaMapper.toEntity(req)));
    }
}
