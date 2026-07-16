package it.case_vacanze.manager.repository;


    
import org.springframework.data.jpa.repository.JpaRepository;
import it.case_vacanze.manager.entity.Offerta;


public interface OffertaRepository extends JpaRepository<Offerta, Integer> {}


