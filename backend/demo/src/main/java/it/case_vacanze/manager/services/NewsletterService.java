package it.case_vacanze.manager.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.case_vacanze.manager.dto.request.NewsletterRequest;
import it.case_vacanze.manager.dto.response.NewsletterResponse;
import it.case_vacanze.manager.entity.Newsletter;
import it.case_vacanze.manager.exception.ConflittoException;
import it.case_vacanze.manager.mapper.NewsletterMapper;
import it.case_vacanze.manager.repository.NewsletterRepository;

@Service
@Transactional(readOnly = true)
public class NewsletterService {

    private static final Logger log = LoggerFactory.getLogger(NewsletterService.class);

    private final NewsletterRepository newsletterRepository;
    private final EmailService emailService;

    public NewsletterService(NewsletterRepository newsletterRepository, EmailService emailService) {
        this.newsletterRepository = newsletterRepository;
        this.emailService = emailService;
    }

    public List<NewsletterResponse> findAll() {
        return newsletterRepository.findAll().stream().map(NewsletterMapper::toResponse).toList();
    }

    @Transactional
    public NewsletterResponse iscrivi(NewsletterRequest req) {
        if (newsletterRepository.existsByEmail(req.email())) {
            throw new ConflittoException("Email già iscritta alla newsletter");
        }
        Newsletter iscrizione = newsletterRepository.save(new Newsletter(req.email()));

        // Se l'email di benvenuto non parte, l'iscrizione resta comunque valida
        try {
            emailService.inviaEmail(req.email(),
                    "Benvenuto alla Newsletter di Case Vacanze!",
                    "Grazie per esserti iscritto alla nostra newsletter! Riceverai aggiornamenti su offerte e novità.");
        } catch (Exception e) {
            log.warn("Invio email di benvenuto a {} fallito: {}", req.email(), e.getMessage());
        }

        return NewsletterMapper.toResponse(iscrizione);
    }
}
