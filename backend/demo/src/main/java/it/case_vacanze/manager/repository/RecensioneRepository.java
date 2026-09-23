package it.case_vacanze.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.case_vacanze.manager.entity.Recensioni;

public interface RecensioneRepository extends JpaRepository<Recensioni, Integer> {
}
