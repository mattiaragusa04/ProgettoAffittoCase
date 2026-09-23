package it.case_vacanze.manager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.case_vacanze.manager.dto.request.NewsletterRequest;
import it.case_vacanze.manager.dto.response.NewsletterResponse;
import it.case_vacanze.manager.services.NewsletterService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/newsletter")
public class NewsletterController {

    private final NewsletterService newsletterService;

    public NewsletterController(NewsletterService newsletterService) {
        this.newsletterService = newsletterService;
    }

    @GetMapping
    public List<NewsletterResponse> getAllNewsletter() {
        return newsletterService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewsletterResponse iscriviti(@Valid @RequestBody NewsletterRequest req) {
        return newsletterService.iscrivi(req);
    }
}
