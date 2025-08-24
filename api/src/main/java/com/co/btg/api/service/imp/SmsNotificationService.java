package com.co.btg.api.service.imp;

import org.springframework.stereotype.Service;

import com.co.btg.api.config.TwilioProperties;
import com.twilio.rest.api.v2010.account.Message;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class SmsNotificationService implements NotificationService {

    private final TwilioProperties twilioProperties;

    @Override
    public void notifyUser(String to, String subject, String message) {
        Message.creator(
                new com.twilio.type.PhoneNumber(to),
                new com.twilio.type.PhoneNumber(twilioProperties.getFromNumber()),
                message
        ).create();
    }
}