package com.co.btg.api.service.imp;

import org.springframework.stereotype.Service;

import com.co.btg.api.enums.NotificationType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationFactory {

    private final EmailNotificationService emailNotificationService;
    private final SmsNotificationService smsNotificationService;

    public NotificationService getService(NotificationType type) {
        return switch (type) {
            case EMAIL -> emailNotificationService;
            case SMS -> smsNotificationService;
        };
    }
}
