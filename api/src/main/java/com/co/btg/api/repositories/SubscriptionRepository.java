package com.co.btg.api.repositories;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.co.btg.api.models.Subscription;

import java.util.List;

@EnableScan
public interface SubscriptionRepository extends CrudRepository<Subscription, String> {
    List<Subscription> findByUserId(String userId);
    List<Subscription> findByFundId(String fundId);
}
