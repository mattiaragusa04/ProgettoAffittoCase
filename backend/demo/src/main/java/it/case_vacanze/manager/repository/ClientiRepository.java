package it.case_vacanze.manager.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import it.case_vacanze.manager.entity.Clienti;


public interface ClientiRepository extends JpaRepository<Clienti, Integer> {
    Clienti findByEmailAndPassword(String email, String password);
    Clienti findByEmail(String email);
}