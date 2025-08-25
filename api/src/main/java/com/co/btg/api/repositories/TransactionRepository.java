package com.co.btg.api.repositories;

import com.co.btg.api.models.Transaction;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;


public class TransactionRepository extends GenericRepository<Transaction> {

	public TransactionRepository(DynamoDbTable<Transaction> table) {
		super(table);
	}
}
