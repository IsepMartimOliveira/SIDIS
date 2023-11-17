package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.repositories.SubsRepoHttpCustom;
import com.example.psoft_22_23_project.subscriptionsmanagement.repositories.SubscriptionsRepositoryDB;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class SubsManager {
    private final SubscriptionsRepositoryDB dbRepository;
    private final SubsRepoHttpCustom httpRepository;

    @Transactional
    public Optional<Subscriptions> findByName( String auth) throws IOException, URISyntaxException, InterruptedException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int commaIndex = username.indexOf(",");
        String newString;
        if (commaIndex != -1) {
            newString = username.substring(commaIndex + 1);
        } else {
            newString = username;
        }

        // local db
        Optional<Subscriptions> resultFromDB = dbRepository.findByActiveStatus_ActiveAndUser(true ,newString);
        if (resultFromDB.isPresent()) {
            return resultFromDB;
        }
        // n local db
        Optional<Subscriptions> resultFromHTTP = httpRepository.getSubsByNameNotLocally(newString,auth);
        if (resultFromHTTP != null) {
            return resultFromHTTP;
        }
        throw new IllegalArgumentException("Uses of user with name " + newString + " does not exist");
    }


    public Subscriptions create(CreateSubscriptionsRequest resource, String auth) throws URISyntaxException, IOException, InterruptedException {
        return httpRepository.create(auth,resource);
    }

    public Subscriptions save(Subscriptions obj) {
        return dbRepository.save(obj);
    }

    public Optional<Subscriptions> findByActiveStatus_ActiveAndUser(boolean b, String user) {
        return dbRepository.findByActiveStatus_ActiveAndUser(b,user);
    }

    public Optional<Subscriptions> isPresent(String auth, String newString) throws URISyntaxException, IOException, InterruptedException {
        return httpRepository.isPresent(auth,newString);
    }

    public PlansDetails subExistLocal(String plan) throws URISyntaxException, IOException, InterruptedException {
        return httpRepository.subExistLocal(plan);
    }

    public PlansDetails subExistNotLocal( String auth) throws URISyntaxException, IOException, InterruptedException {
        return httpRepository.subExistNotLocal(auth);
    }

    public Subscriptions cancelSub(String auth, long desiredVersion) throws URISyntaxException, IOException, InterruptedException {
        return httpRepository.cancelSub(auth,desiredVersion);
    }

    public Subscriptions renewSub(String auth, long desiredVersion) throws URISyntaxException, IOException, InterruptedException {

        return httpRepository.renewSub(auth,desiredVersion);
    }

    public Subscriptions changePlan(String auth, String name, long desiredVersion) throws URISyntaxException, IOException, InterruptedException {
        return httpRepository.changePlan(auth,name,desiredVersion);
    }
}