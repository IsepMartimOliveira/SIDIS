package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.repositories.SubsRepoHttpCustom;
import com.example.psoft_22_23_project.subscriptionsmanagement.repositories.SubscriptionsRepositoryDB;
import lombok.RequiredArgsConstructor;
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
    public Optional<Subscriptions> findByName(String name, String auth) throws IOException, URISyntaxException, InterruptedException {
        // local db
        Optional<Subscriptions> resultFromDB = dbRepository.findByActiveStatus_ActiveAndUser(true ,name);

        if (resultFromDB.isPresent()) {
            return resultFromDB;
        } else {
            // nao localemente
            Optional<Subscriptions> resultFromHTTP = httpRepository.getSubsByNameNotLocally(name,auth);
            return resultFromHTTP;
        }
    }


    public Subscriptions planExists(CreateSubscriptionsRequest resource, String auth) throws URISyntaxException, IOException, InterruptedException {
        return httpRepository.planExists(auth,resource);
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

    public PlansDetails subExistNotLocal(String newString, String auth) throws URISyntaxException, IOException, InterruptedException {
        return httpRepository.subExistNotLocal(newString,auth);
    }

    public Subscriptions cancelSub(String newString, String auth, long desiredVersion) throws URISyntaxException, IOException, InterruptedException {
        return httpRepository.cancelSub(newString,auth,desiredVersion);
    }

    public Subscriptions renewSub(String newString, String auth, long desiredVersion) throws URISyntaxException, IOException, InterruptedException {

        return httpRepository.renewSub(newString,auth,desiredVersion);
    }

    public Subscriptions changePlan(String newString, String auth, String name, long desiredVersion) throws URISyntaxException, IOException, InterruptedException {
        return httpRepository.changePlan(newString,auth,name,desiredVersion);
    }
}