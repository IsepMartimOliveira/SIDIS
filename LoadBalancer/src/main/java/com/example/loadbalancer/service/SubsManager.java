package com.example.loadbalancer.service;

import com.example.loadbalancer.model.Subscriptions;

import java.util.Optional;

public interface SubsManager {
     Subscriptions save(Subscriptions obj);


     Iterable<Subscriptions> findAll();

    Optional<Subscriptions> findByActiveStatus_ActiveAndUser(String b, String user);

}

