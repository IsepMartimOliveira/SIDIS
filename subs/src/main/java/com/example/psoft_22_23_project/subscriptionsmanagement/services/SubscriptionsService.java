package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

public interface SubscriptionsService {

    Iterable<Subscriptions> findAll();


    Subscriptions create(CreateSubscriptionsRequest resource) throws URISyntaxException, IOException, InterruptedException;

    //void delete(Long id);
    Optional<Subscriptions> findSubByUserAndPlan(String planName, String user);

    Subscriptions cancelSubscription(long desiredVersion);

    Subscriptions renewAnualSubscription(long desiredVersion);

    Subscriptions changePlan(long desiredVersion, String name);

    void migrateAllToPlan(long desiredVersion,String actualPlan, String newPlan);
    PlansDetails planDetails();

}
