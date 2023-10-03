package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;

public interface SubscriptionsService {

    Iterable<Subscriptions> findAll();


    Subscriptions create(CreateSubscriptionsRequest resource);

    //void delete(Long id);

    Subscriptions cancelSubscription(long desiredVersion);

    Subscriptions renewAnualSubscription(long desiredVersion);

    Subscriptions changePlan(long desiredVersion, String name);

    void migrateAllToPlan(long desiredVersion,String actualPlan, String newPlan);
    PlansDetails planDetails();

}
