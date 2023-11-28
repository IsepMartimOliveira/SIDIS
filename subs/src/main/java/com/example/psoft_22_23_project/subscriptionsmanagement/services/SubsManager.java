package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

public interface SubsManager {
     Optional<Subscriptions> findSub(String auth,String name);
     Optional<Subscriptions> findByActiveStatus_ActiveAndUser(boolean b, String user);
    Subscriptions save(Subscriptions obj);


}
