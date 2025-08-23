package com.co.btg.api.repositories;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.co.btg.api.models.Fund;

@EnableScan
public interface FundRepository extends CrudRepository<Fund, String> {
}

