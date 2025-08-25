package com.co.btg.api.service.imp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)

class EmailNotificationServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailNotificationService emailNotificationService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> emailCaptor;


    @Test
    void notifyUser_ShouldSendEmailWithCorrectParameters() {
        // Arrange
        String to = "usuario@example.com";
        String subject = "Test Subject";
        String message = "Test Message";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        emailNotificationService.notifyUser(to, subject, message);

        // Assert
        verify(mailSender).send(emailCaptor.capture());
        
        SimpleMailMessage capturedEmail = emailCaptor.getValue();
        assertNotNull(capturedEmail);
        assertEquals(to, capturedEmail.getTo()[0]);
        assertEquals(subject, capturedEmail.getSubject());
        assertEquals(message, capturedEmail.getText());
    }

    @Test
    void notifyUser_WithEmptyFields_ShouldStillSendEmail() {
        // Arrange
        String to = "usuario@example.com";
        String subject = "";
        String message = "";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        emailNotificationService.notifyUser(to, subject, message);

        // Assert
        verify(mailSender).send(emailCaptor.capture());
        
        SimpleMailMessage capturedEmail = emailCaptor.getValue();
        assertNotNull(capturedEmail);
        assertEquals(to, capturedEmail.getTo()[0]);
        assertEquals(subject, capturedEmail.getSubject());
        assertEquals(message, capturedEmail.getText());
    }

    @Test
    void notifyUser_WhenMailSenderThrowsException_ShouldPropagateException() {
        // Arrange
        String to = "usuario@example.com";
        String subject = "Test Subject";
        String message = "Test Message";

        doThrow(new RuntimeException("Error sending email"))
            .when(mailSender).send(any(SimpleMailMessage.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            emailNotificationService.notifyUser(to, subject, message)
        );
        
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
