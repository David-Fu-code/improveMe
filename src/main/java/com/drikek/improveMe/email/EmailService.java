package com.drikek.improveMe.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class EmailService implements EmailSender{

    private final JavaMailSender emailSender;

    @Override
    public void send(String to, String emailContent) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(emailContent, true);
            helper.setTo(to);
            helper.setSubject("Confirmed your email");
            helper.setFrom("example@example.com");

            emailSender.send(mimeMessage);

        }catch (MessagingException e){
            throw new IllegalStateException("Failed to send email" + e);
        }
    }
}
