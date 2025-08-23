package com.co.btg.api.service;

import org.springframework.stereotype.Service;

import com.co.btg.api.models.User;

@Service
public class NotificationService {

    public void send(User user, String message) {
        String channel = user.getPreferredNotification(); // "EMAIL" o "SMS"
        if ("SMS".equalsIgnoreCase(channel)) {
            System.out.println("📱 Enviando SMS a " + user.getUserId() + ": " + message);
        } else {
            System.out.println("📧 Enviando EMAIL a " + user.getUserId() + ": " + message);
        }
    }
}
