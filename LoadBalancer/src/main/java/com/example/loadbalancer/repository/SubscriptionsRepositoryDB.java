package com.example.loadbalancer.repository;


import com.example.loadbalancer.model.Plans;
import com.example.loadbalancer.model.Subscriptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Configuration
public interface SubscriptionsRepositoryDB extends CrudRepository<Subscriptions,Long> {
    List<Subscriptions> findAll();

    Optional<Subscriptions> findByActiveStatusAndUser(String b, String user);
    Optional<Subscriptions> findByUser(String user);
}

