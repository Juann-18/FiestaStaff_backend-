package com.backend.fiestaStaff.service;

import com.backend.fiestaStaff.model.Event;
import com.backend.fiestaStaff.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async("emailExecutor")
    public void sendEventConfirmation(Event event, User user) {
        try {
            log.info("Attempting to send email to: {}", user.getEmail());

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            String subject = "Evento creado: " + event.getEventType().getName();
            String content = "Estimado/a " + user.getFirstName() + ",\n\n"
                    + "Le confirmamos que su evento ha sido creado exitosamente en nuestro sistema.\n\n"
                    + "DETALLES DEL EVENTO\n"
                    + "─────────────────────────────────────\n"
                    + "Tipo de evento:        " + event.getEventType().getName() + "\n"
                    + "Ubicación:             " + event.getLocation() + "\n"
                    + "Fecha:                 " + event.getScheduledAt().format(dateFormatter) + "\n"
                    + "Hora de inicio:        " + event.getStartTime().format(timeFormatter) + "\n"
                    + "Hora de finalización:  " + event.getEndTime().format(timeFormatter) + "\n\n"
                    + "Si tiene alguna pregunta o necesita realizar cambios en su evento,\n"
                    + "no dude en contactarnos a través de nuestro equipo de soporte.\n\n"
                    + "Cordialmente,\n"
                    + "Equipo FiestaStaff";

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject(subject);
            message.setText(content);

            mailSender.send(message);
            log.info("Email sent successfully to: {}", user.getEmail());
        } catch (MailException e) {
            log.error("Failed to send email to: {} - Error: {}", user.getEmail(), e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error sending email to: {} - Error: {}", user.getEmail(), e.getMessage());
        }
    }
}