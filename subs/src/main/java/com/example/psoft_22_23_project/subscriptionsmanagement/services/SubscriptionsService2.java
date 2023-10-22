package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;

import java.util.Optional;

public interface SubscriptionsService2 {


    Optional<Subscriptions> findSubByUserAndPlan( String user);
}
