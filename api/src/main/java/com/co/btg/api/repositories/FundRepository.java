package com.co.btg.api.repositories;

import org.springframework.stereotype.Repository;

import com.co.btg.api.models.Fund;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

@Repository
public class FundRepository extends GenericRepository<Fund> {

	public FundRepository(DynamoDbTable<Fund> table) {
		super(table);
	}
}
