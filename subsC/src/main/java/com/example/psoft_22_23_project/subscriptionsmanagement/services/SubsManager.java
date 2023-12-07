package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

public interface SubsManager {

     Optional<Subscriptions> findIfUserHavesSub(String auth,String user) ;
     void findIfUserDoesNotHavesSub(String auth, String user);
     Subscriptions save(Subscriptions obj);
     Optional<Subscriptions> findByActiveStatus_ActiveAndUser(boolean b, String user);

}
