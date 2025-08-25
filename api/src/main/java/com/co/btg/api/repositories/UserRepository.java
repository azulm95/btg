package com.co.btg.api.repositories;


import com.co.btg.api.models.User;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

public class UserRepository extends GenericRepository<User> {
	public UserRepository(DynamoDbTable<User> userTable) {
		super(userTable);
	}
}
