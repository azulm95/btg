package com.co.btg.api.repositories;

import com.co.btg.api.models.Subscription;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

public class SubscriptionRepository extends GenericRepository<Subscription> {

	public SubscriptionRepository(DynamoDbTable<Subscription> table) {
		super(table);
	}
	
}
