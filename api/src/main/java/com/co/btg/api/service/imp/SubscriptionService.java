package com.co.btg.api.service.imp;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.co.btg.api.enums.NotificationType;
import com.co.btg.api.exceptions.FundNotFoundException;
import com.co.btg.api.exceptions.InsufficientBalanceException;
import com.co.btg.api.exceptions.SubscriptionNotFoundException;
import com.co.btg.api.exceptions.UserNotFoundException;
import com.co.btg.api.models.Fund;
import com.co.btg.api.models.Subscription;
import com.co.btg.api.models.Transaction;
import com.co.btg.api.models.User;
import com.co.btg.api.repositories.GenericRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService {


    private final GenericRepository<User> userRepository;
    private final GenericRepository<Fund> fundRepository;
    private final GenericRepository<Subscription> subscriptionRepository;
    private final GenericRepository<Transaction> transactionRepository;
    private final NotificationFactory notificationFactory;

    // Suscribirse a un fondo
    public Subscription subscribe(String userId, String fundId, Double amount) {
    	User user = userRepository.findById(userId)
    	        .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + userId));

        Fund fund = fundRepository.findById(fundId)
                .orElseThrow(() -> new FundNotFoundException("Fondo no encontrado: " + fundId));

        if (amount < fund.getMinAmount()) {
            throw new InsufficientBalanceException(
                "El monto mínimo para vincularse al fondo " + fund.getName() + " es " + fund.getMinAmount()
            );
        }
        if (user.getBalance() < amount) {
            throw new InsufficientBalanceException(
                "No tiene saldo disponible para vincularse al fondo " + fund.getName()
            );
        }
        // Crear suscripción
        Subscription subscription = Subscription.builder()
                .subscriptionId(UUID.randomUUID().toString())
                .userId(userId)
                .fundId(fundId)
                .amount(amount)
                .active(true)
                .startDate(Instant.now().toString())
                .build();
        subscriptionRepository.save(subscription);

        // Descontar saldo del usuario
        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);

        // Registrar transacción
        Transaction tx = Transaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .userId(userId)
                .fundId(fundId)
                .type("OPEN")
                .amount(amount)
                .date(Instant.now().toString())
                .build();
        transactionRepository.save(tx);

        // Notificación simulada
        
       if(user.getPreferredNotification().equalsIgnoreCase("EMAIL")) {
    	   var notificationService = notificationFactory.getService(NotificationType.EMAIL);
       		notificationService.notifyUser(user.getEmail(), "Btg Notificacion", "Saludos: "+user.getName() +" Se ha suscrito exitosamente al fondo " + fund.getName());
       }else {
    	   var notificationService = notificationFactory.getService(NotificationType.SMS);
      		notificationService.notifyUser(user.getPhone(), "Btg Notificacion", "Saludos: "+user.getName() +" Se ha suscrito exitosamente al fondo " + fund.getName());
       }
    	
    
    

        
        return subscription;
    }

    // Cancelar suscripción
    public void cancel(String subscriptionId) {
    	  Subscription subscription = subscriptionRepository.findById(subscriptionId)
                  .orElseThrow(() -> new SubscriptionNotFoundException(
                          "Suscripción no encontrada con id: " + subscriptionId
                  ));

        if (!subscription.getActive()) {
            throw new RuntimeException("La suscripción ya está cancelada");
        }

        User user = userRepository.findById(subscription.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + subscription.getUserId()));
        Fund fund = fundRepository.findById(subscription.getFundId())
                .orElseThrow(() -> new RuntimeException("Fondo no encontrado"));

        // Cancelar
        subscription.setActive(false);
        subscription.setEndDate(Instant.now().toString());
        subscriptionRepository.save(subscription);

        // Devolver saldo
        user.setBalance(user.getBalance() + subscription.getAmount());
        userRepository.save(user);

        // Registrar transacción
        Transaction tx = Transaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .userId(user.getUserId())
                .fundId(fund.getFundId())
                .type("CANCEL")
                .amount(subscription.getAmount())
                .date(Instant.now().toString())
                .build();
        transactionRepository.save(tx);

        // Notificación simulada
        
        if(user.getPreferredNotification().equalsIgnoreCase("EMAIL")) {
     	   var notificationService = notificationFactory.getService(NotificationType.EMAIL);
        		notificationService.notifyUser(user.getEmail(), "Btg Notificacion", "Saludos: "+user.getName() + " Ha cancelado la suscripción al fondo " + fund.getName());
        }else {
     	   var notificationService = notificationFactory.getService(NotificationType.SMS);
       		notificationService.notifyUser(user.getPhone(), "Btg Notificacion", "Saludos: "+user.getName() + " Ha cancelado la suscripción al fondo " + fund.getName());
        }
        
    }

    	public List<Subscription> getSubscriptionsByUserId(String userId){
    		return subscriptionRepository.findByField("userId", userId);
    	}

}

