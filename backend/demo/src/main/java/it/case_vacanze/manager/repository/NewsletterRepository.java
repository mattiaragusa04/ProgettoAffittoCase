package it.case_vacanze.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.case_vacanze.manager.entity.Newsletter;

public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {
    boolean existsByEmail(String email);
}
