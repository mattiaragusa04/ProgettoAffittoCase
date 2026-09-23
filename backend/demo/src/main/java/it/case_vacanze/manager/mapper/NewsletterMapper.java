package it.case_vacanze.manager.mapper;

import it.case_vacanze.manager.dto.response.NewsletterResponse;
import it.case_vacanze.manager.entity.Newsletter;

public final class NewsletterMapper {

    private NewsletterMapper() {}

    public static NewsletterResponse toResponse(Newsletter newsletter) {
        return new NewsletterResponse(newsletter.getId(), newsletter.getEmail());
    }
}
