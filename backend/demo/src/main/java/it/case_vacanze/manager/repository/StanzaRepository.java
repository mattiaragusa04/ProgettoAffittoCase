package it.case_vacanze.manager.repository;


    
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import it.case_vacanze.manager.entity.Stanze;

@Repository
public interface StanzaRepository extends JpaRepository<Stanze, Integer> {}


