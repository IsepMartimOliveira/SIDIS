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

    private final SubsManagerImlp subsManager;
    private final CreateSubscriptionsMapper  createSubscriptionsMapper;

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

    @Override
    public Subscriptions create(final CreateSubscriptionsRequest resource,String auth) throws URISyntaxException, IOException, InterruptedException {
        String user = getUsername();
        subsManager.findIfUserDoesNotHavesSub(auth,user);
        subsManager.checkIfPlanExist(resource.getName());
        Subscriptions obj = createSubscriptionsMapper.create(user,resource.getName(),resource);
        return subsManager.save(obj);
    }

    @Override
    public Subscriptions cancelSubscription(String auth,final long desiredVersion) throws URISyntaxException, IOException, InterruptedException {
        String user = getUsername();
        Optional<Subscriptions> subscriptions = subsManager.findIfUserHavesSub(auth,user);
        Subscriptions subscriptions1 = subscriptions.get();
        subscriptions1.cancelSubscription(desiredVersion);
        return subsManager.save(subscriptions1);

    }

    @Override
    public Subscriptions renewAnualSubscription(String auth,final long desiredVersion) throws URISyntaxException, IOException, InterruptedException {
        String user = getUsername();
        Optional<Subscriptions> subscriptions = subsManager.findIfUserHavesSub(auth,user);
        Subscriptions subscriptions1 = subscriptions.get();
        subscriptions1.renewSub(desiredVersion);
        return subsManager.save(subscriptions1);

    }
    @Override
    public Subscriptions changePlan(final long desiredVersion, final String name,final String auth) throws URISyntaxException, IOException, InterruptedException {

        String user = getUsername();
        Optional<Subscriptions> subscriptions = subsManager.findIfUserHavesSub(auth,user);
        Subscriptions subscriptions1 = subscriptions.get();
        subscriptions1.changePlan(desiredVersion,name);
        return subsManager.save(subscriptions1);
    }


}
