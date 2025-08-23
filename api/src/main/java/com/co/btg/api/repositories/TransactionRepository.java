package com.co.btg.api.repositories;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.co.btg.api.models.Transaction;

import java.util.List;

@EnableScan
public interface TransactionRepository extends CrudRepository<Transaction, String> {
    List<Transaction> findByUserId(String userId);
    List<Transaction> findByFundId(String fundId);
}
