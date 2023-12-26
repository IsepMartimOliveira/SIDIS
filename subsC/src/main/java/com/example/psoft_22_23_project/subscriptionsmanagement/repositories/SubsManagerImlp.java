package com.example.psoft_22_23_project.subscriptionsmanagement.repositories;

import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.repositories.SubsRepoHttpCustom;
import com.example.psoft_22_23_project.subscriptionsmanagement.repositories.SubscriptionsRepositoryDB;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class SubsManagerImlp implements SubsManager {
    private final SubscriptionsRepositoryDB dbRepository;

    @Transactional
    public Optional<Subscriptions> findIfUserHavesSub( String auth,String newString){
        Optional<Subscriptions> resultFromDB = dbRepository.findByActiveStatus_ActiveAndUser(true ,newString);
        if (resultFromDB.isPresent()) {
            return resultFromDB;
        }
        throw new IllegalArgumentException("Sub of user with name " + newString + " does not exist");
    }
    @Transactional
    public void findIfUserDoesNotHavesSub(String newString) {
        Optional<Subscriptions> resultFromDB = dbRepository.findByActiveStatus_ActiveAndUser(true ,newString);
        if (resultFromDB.isPresent()) {
            throw new IllegalArgumentException("User already haves sub");
        }

    }
    public Optional<Subscriptions> findByActiveStatus_ActiveAndUser(boolean b, String user) {
        return dbRepository.findByActiveStatus_ActiveAndUser(b,user);
    }
    public Optional<Subscriptions> findByUser(String name){
        Optional<Subscriptions> resultFromDB = dbRepository.findByUser(name);
        return resultFromDB;
    }
    public Subscriptions save(Subscriptions obj) {
        return dbRepository.save(obj);
    }

}
