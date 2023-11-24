package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

public interface SubscriptionsService {

    Subscriptions create(CreateSubscriptionsRequest resource, String auth) throws URISyntaxException, IOException, InterruptedException;

    Subscriptions cancelSubscription(String auth,long desiredVersion) throws URISyntaxException, IOException, InterruptedException;

    Subscriptions renewAnualSubscription(String auth,long desiredVersion) throws URISyntaxException, IOException, InterruptedException;

    Subscriptions changePlan(long desiredVersion, String name, String auth) throws URISyntaxException, IOException, InterruptedException;

}
