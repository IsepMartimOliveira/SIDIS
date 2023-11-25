package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionsServiceImpl implements SubscriptionsService {

    private final SubsManager subsManager;
    private final CreateSubscriptionsMapper createSubscriptionsMapper;


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
    private String getUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int commaIndex = username.indexOf(",");
        String newString;
        if (commaIndex != -1) {
            newString = username.substring(commaIndex + 1);
        } else {
            newString = username;
        }
        return  newString;
    }
    public void storeSub(CreateSubscriptionsRequest subsRequest) {
        String user = getUsername();
        subsManager.findByActiveStatus_ActiveAndUser(true,user);
        Subscriptions obj=createSubscriptionsMapper.create(user,subsRequest.getName(),subsRequest);
        subsManager.save(obj);
    }
}






