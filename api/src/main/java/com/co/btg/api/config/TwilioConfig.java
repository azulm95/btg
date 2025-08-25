package com.co.btg.api.config;

import org.springframework.stereotype.Component;

import com.twilio.Twilio;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TwilioConfig {

    private final TwilioProperties twilioProperties; // clase que contiene accountSid y authToken

    @PostConstruct
    public void initTwilio() {
        Twilio.init(twilioProperties.getAccountSid(), twilioProperties.getAuthToken());
    }
}
