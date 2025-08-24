package com.co.btg.api.service.imp;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.co.btg.api.models.User;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailNotificationService implements NotificationService{
	
	 private final JavaMailSender mailSender;
  
    @Override
    public void notifyUser(String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }
}
