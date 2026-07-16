package it.case_vacanze.manager.repository;
import org.springframework.data.jpa.repository.JpaRepository;


import it.case_vacanze.manager.entity.Prenotazione;


public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Integer> {

}