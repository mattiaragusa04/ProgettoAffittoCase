package it.case_vacanze.manager.repository;

import java.time.LocalDate;
import java.util.List;

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


    // Prenotazioni di un cliente, dalla più recente. Serve @Query: il campo si chiama cliente_id
    // e Spring non può ricavarlo dal nome del metodo (cercherebbe un campo "clienteId")
    @Query("SELECT p FROM Prenotazione p WHERE p.cliente_id = :clienteId ORDER BY p.data_check_in DESC")
    List<Prenotazione> findByClienteId(@Param("clienteId") Integer clienteId);
}
