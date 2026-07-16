package it.case_vacanze.manager.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void inviaEmail(String destinatario, String oggetto, String testo) {
        SimpleMailMessage messaggio = new SimpleMailMessage();
        messaggio.setTo(destinatario);
        messaggio.setSubject(oggetto);
        messaggio.setText(testo);
        mailSender.send(messaggio);
    }
}
