package com.backend.fiestaStaff.service;

import com.backend.fiestaStaff.model.Event;
import com.backend.fiestaStaff.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            String content = "Hola " + user.getFirstName() + ",\n\n"
                    + "Tu evento ha sido creado exitosamente:\n\n"
                    + "Tipo: " + event.getEventType().getName() + "\n"
                    + "Ubicacion: " + event.getLocation() + "\n"
                    + "Fecha: " + event.getScheduledAt().format(dateFormatter) + "\n"
                    + "Hora inicio: " + event.getStartTime().format(timeFormatter) + "\n"
                    + "Hora fin: " + event.getEndTime().format(timeFormatter) + "\n\n"
                    + "Saludos,\nFiestaStaff";

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("juanjoseagudelogutierrez794@gmail.com");
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
