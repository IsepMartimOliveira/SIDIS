package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionsServiceImpl implements SubscriptionsService {

    private final SubsManager subsManager;

    @Override
    public Optional<Subscriptions> findSubByUserExternal(String user){
        return subsManager.findByActiveStatus_ActiveAndUser(true, user);
    }

    @SneakyThrows
    @Override
    public Optional<PlansDetails> planDetails(String auth) {

        Optional<Subscriptions> subscription = subsManager.findSub(auth);

        Optional<PlansDetails> objLocal = subsManager.findPlan(subscription.get().getPlan());

        return objLocal;

    }

}






