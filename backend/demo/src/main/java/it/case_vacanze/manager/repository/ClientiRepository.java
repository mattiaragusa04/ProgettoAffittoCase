package it.case_vacanze.manager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.case_vacanze.manager.entity.Clienti;

public interface ClientiRepository extends JpaRepository<Clienti, Integer> {
    Optional<Clienti> findByEmail(String email);
    boolean existsByEmail(String email);
}
