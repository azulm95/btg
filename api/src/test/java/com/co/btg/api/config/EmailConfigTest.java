package com.co.btg.api.config;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.util.ReflectionTestUtils;

class EmailConfigTest {

    @Test
    void javaMailSender_ShouldConfigureMailSenderCorrectly() {
        // Arrange
        EmailConfig emailConfig = new EmailConfig();
        ReflectionTestUtils.setField(emailConfig, "host", "smtp.test.com");
        ReflectionTestUtils.setField(emailConfig, "port", 587);
        ReflectionTestUtils.setField(emailConfig, "username", "testuser");
        ReflectionTestUtils.setField(emailConfig, "password", "testpass");

        // Act
        JavaMailSender mailSender = emailConfig.javaMailSender();

        // Assert
        assertNotNull(mailSender);
        assertTrue(mailSender instanceof JavaMailSenderImpl);

        JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;
        assertEquals("smtp.test.com", impl.getHost());
        assertEquals(587, impl.getPort());
        assertEquals("testuser", impl.getUsername());
        assertEquals("testpass", impl.getPassword());

        Properties props = impl.getJavaMailProperties();
        assertEquals("smtp", props.get("mail.transport.protocol"));
        assertEquals("true", props.get("mail.smtp.auth"));
        assertEquals("true", props.get("mail.smtp.starttls.enable"));
        assertEquals("true", props.get("mail.debug"));
    }
}