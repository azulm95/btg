package com.co.btg.api.service.imp;

import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.co.btg.api.enums.NotificationType;

class NotificationFactoryTest {

    private EmailNotificationService emailNotificationService;
    private SmsNotificationService smsNotificationService;
    private NotificationFactory factory;

    @BeforeEach
    void setUp() {
        emailNotificationService = Mockito.mock(EmailNotificationService.class);
        smsNotificationService = Mockito.mock(SmsNotificationService.class);
        factory = new NotificationFactory(emailNotificationService, smsNotificationService);
    }

    @Test
    void getService_shouldReturnEmailService_whenTypeIsEmail() {
        NotificationService result = factory.getService(NotificationType.EMAIL);

        assertSame(emailNotificationService, result,
                "Debe devolver la instancia de EmailNotificationService");
    }

    @Test
    void getService_shouldReturnSmsService_whenTypeIsSms() {
        NotificationService result = factory.getService(NotificationType.SMS);

        assertSame(smsNotificationService, result,
                "Debe devolver la instancia de SmsNotificationService");
    }
}