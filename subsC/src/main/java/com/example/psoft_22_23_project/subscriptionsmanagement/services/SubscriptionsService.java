package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

public interface SubscriptionsService {

    Iterable<Subscriptions> findAll();


    Subscriptions create(CreateSubscriptionsRequest resource, String auth) throws URISyntaxException, IOException, InterruptedException;

    //void delete(Long id);
    Optional<Subscriptions> findSubByUser( String user) throws IOException, URISyntaxException, InterruptedException;

    Subscriptions cancelSubscription(String auth,long desiredVersion) throws URISyntaxException, IOException, InterruptedException;

    Subscriptions renewAnualSubscription(String auth,long desiredVersion) throws URISyntaxException, IOException, InterruptedException;

    Subscriptions changePlan(long desiredVersion, String name, String auth) throws URISyntaxException, IOException, InterruptedException;

    void migrateAllToPlan(long desiredVersion,String actualPlan, String newPlan);
    PlansDetails planDetails(String auth);

    Optional<Subscriptions> findSubByUserExternal(String user) throws IOException, URISyntaxException, InterruptedException;
}
