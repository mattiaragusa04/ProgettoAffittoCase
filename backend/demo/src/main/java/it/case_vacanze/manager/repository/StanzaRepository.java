package it.case_vacanze.manager.repository;


    
import org.springframework.data.jpa.repository.JpaRepository;
import it.case_vacanze.manager.entity.Stanze;


public interface StanzaRepository extends JpaRepository<Stanze, Integer> {}


