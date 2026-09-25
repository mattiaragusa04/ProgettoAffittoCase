package it.case_vacanze.manager.repository;

import it.case_vacanze.manager.entity.Stanze;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface StanzeRepository extends JpaRepository<Stanze, Integer> {

    // Query nativa per trovare stanze con abbastanza posti e senza prenotazioni sovrapposte
    @Query(value = "SELECT * FROM stanze s " +
                   "WHERE s.numero_posti >= :guests " +
                   "AND s.disponibile = 1 " +
                   "AND s.id NOT IN (" +
                   "    SELECT p.stanza_id FROM prenotazione p " +
                   "    WHERE (p.data_check_in < :checkOut AND p.data_check_out > :checkIn)" +
                   ")", nativeQuery = true)
    List<Stanze> findAvailableRooms(@Param("checkIn") LocalDate checkIn, 
                                    @Param("checkOut") LocalDate checkOut, 
                                    @Param("guests") Integer guests);
    

                                    
}