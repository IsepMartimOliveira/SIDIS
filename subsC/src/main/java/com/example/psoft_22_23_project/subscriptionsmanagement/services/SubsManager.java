package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

public interface SubsManager {

     Optional<Subscriptions> findByName(String auth) throws IOException, URISyntaxException, InterruptedException ;
     Subscriptions create(CreateSubscriptionsRequest resource, String auth) throws URISyntaxException, IOException, InterruptedException ;
     Subscriptions save(Subscriptions obj);

     Optional<Subscriptions> findByActiveStatus_ActiveAndUser(boolean b, String user);
     Optional<Subscriptions> isPresent(String auth, String newString) throws URISyntaxException, IOException, InterruptedException ;
     PlansDetails subExistLocal(String plan) throws URISyntaxException, IOException, InterruptedException ;
     PlansDetails subExistNotLocal( String auth) throws URISyntaxException, IOException, InterruptedException ;
     Subscriptions cancelSub(String auth, long desiredVersion) throws URISyntaxException, IOException, InterruptedException ;
     Subscriptions renewSub(String auth, long desiredVersion) throws URISyntaxException, IOException, InterruptedException ;
     Subscriptions changePlan(String auth, String name, long desiredVersion) throws URISyntaxException, IOException, InterruptedException ;

    }
