package com.co.btg.api.service.imp;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.co.btg.api.config.TwilioProperties;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

@ExtendWith(MockitoExtension.class)
class SmsNotificationServiceTest {

    @Mock
    private TwilioProperties twilioProperties;

    @Mock
    private MessageCreator messageCreator;

    @InjectMocks
    private SmsNotificationService smsNotificationService;

    private static final String FROM_NUMBER = "+1234567890";
    private static final String TO_NUMBER = "+0987654321";
    private static final String TEST_MESSAGE = "Test message";

    @BeforeEach
    void setUp() {
        when(twilioProperties.getFromNumber()).thenReturn(FROM_NUMBER);
    }

    @Test
    void notifyUser_ShouldSendSmsSuccessfully() {
        // Arrange
        try (MockedStatic<Message> mockedMessage = mockStatic(Message.class)) {
            Message expectedMessage = mock(Message.class);
            when(messageCreator.create()).thenReturn(expectedMessage);
            mockedMessage.when(() -> Message.creator(
                    any(PhoneNumber.class),
                    any(PhoneNumber.class),
                    eq(TEST_MESSAGE)
            )).thenReturn(messageCreator);

            // Act
            assertDoesNotThrow(() -> 
                smsNotificationService.notifyUser(TO_NUMBER, "Test subject", TEST_MESSAGE)
            );

            // Assert
            mockedMessage.verify(() -> Message.creator(
                    new PhoneNumber(TO_NUMBER),
                    new PhoneNumber(FROM_NUMBER),
                    TEST_MESSAGE
            ));
            verify(messageCreator).create();
        }
    }

    @Test
    void notifyUser_WhenTwilioThrowsException_ShouldPropagateException() {
        // Arrange
        try (MockedStatic<Message> mockedMessage = mockStatic(Message.class)) {
            when(messageCreator.create()).thenThrow(new RuntimeException("Failed to send SMS"));
            mockedMessage.when(() -> Message.creator(
                    any(PhoneNumber.class),
                    any(PhoneNumber.class),
                    anyString()
            )).thenReturn(messageCreator);

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () ->
                smsNotificationService.notifyUser(TO_NUMBER, "Test subject", TEST_MESSAGE)
            );
            assertEquals("Failed to send SMS", exception.getMessage());
        }
    }

    @Test
    void notifyUser_ShouldUseCorrectPhoneNumbers() {
        // Arrange
        try (MockedStatic<Message> mockedMessage = mockStatic(Message.class)) {
            Message expectedMessage = mock(Message.class);
            when(messageCreator.create()).thenReturn(expectedMessage);
            mockedMessage.when(() -> Message.creator(
                    any(PhoneNumber.class),
                    any(PhoneNumber.class),
                    anyString()
            )).thenReturn(messageCreator);

            // Act
            smsNotificationService.notifyUser(TO_NUMBER, "Test subject", TEST_MESSAGE);

            // Assert
            verify(messageCreator).create();
            mockedMessage.verify(() -> Message.creator(
                    any(PhoneNumber.class),
                    any(PhoneNumber.class),
                    eq(TEST_MESSAGE)
            ));
        }
    }
}
