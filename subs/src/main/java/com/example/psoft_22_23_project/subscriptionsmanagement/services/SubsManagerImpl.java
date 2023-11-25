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
class SubsManagerImpl implements SubsManager{
    private final SubscriptionsRepositoryDB dbRepository;
    private final SubsRepoHttpCustom httpRepository;

    @Transactional
    public Optional<Subscriptions> findSub( String auth) throws IOException, URISyntaxException, InterruptedException {

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
        throw new IllegalArgumentException("Sub of user with name " + newString + " does not exist");
    }

    public Optional<Subscriptions> findByActiveStatus_ActiveAndUser(boolean b, String user) {
        return dbRepository.findByActiveStatus_ActiveAndUser(b,user);
    }
    public Subscriptions save(Subscriptions obj) {
        return dbRepository.save(obj);
    }


    public Optional<PlansDetails> findPlan(String plan) throws URISyntaxException, IOException, InterruptedException {
        return httpRepository.findPlan(plan);
    }


}