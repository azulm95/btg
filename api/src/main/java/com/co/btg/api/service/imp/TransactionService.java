package com.co.btg.api.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.co.btg.api.exceptions.TransactionNotFoundException;
import com.co.btg.api.models.Transaction;
import com.co.btg.api.repositories.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
	
	  private final TransactionRepository transactionRepository;
    // Ver historial
    public List<Transaction> getHistoryByUser(String userId) {
    	List<Transaction> trans=transactionRepository.findByField("userId", userId);
		if(!trans.isEmpty()) {
			return trans;
		}else {
			throw new TransactionNotFoundException("No se encontraron transacciones para el usuario: "+ userId);
		}
    	
    	
    }

}
