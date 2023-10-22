package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.repositories.SubscriptionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionsServiceImpl2 implements SubscriptionsService2 {

    private final SubscriptionsRepository repository;

    @Override
    public Optional<Subscriptions> findSubByUserAndPlan(String user) {
        return repository.findByActiveStatus_ActiveAndUser(true, user);
    }
}






