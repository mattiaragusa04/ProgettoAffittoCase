package it.case_vacanze.manager.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.case_vacanze.manager.entity.Prenotazione;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Integer> {

    // true se la stanza ha già una prenotazione che si sovrappone alle date richieste
    @Query("SELECT COUNT(p) > 0 FROM Prenotazione p " +
           "WHERE p.stanza_id = :stanzaId " +
           "AND p.data_check_in < :checkOut AND p.data_check_out > :checkIn")
    boolean existsSovrapposizione(@Param("stanzaId") Integer stanzaId,
                                  @Param("checkIn") LocalDate checkIn,
                                  @Param("checkOut") LocalDate checkOut);
}
