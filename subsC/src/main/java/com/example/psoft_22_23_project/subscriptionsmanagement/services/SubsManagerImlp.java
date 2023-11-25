package com.example.psoft_22_23_project.subscriptionsmanagement.services;

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
class SubsManagerImlp implements SubsManager{
    private final SubscriptionsRepositoryDB dbRepository;
    private final SubsRepoHttpCustom httpRepository;

    @Transactional
    public Optional<Subscriptions> findIfUserHavesSub( String auth,String newString){

        // local db
        Optional<Subscriptions> resultFromDB = dbRepository.findByActiveStatus_ActiveAndUser(true ,newString);
        if (resultFromDB.isPresent()) {
            return resultFromDB;
        }
        /*
        // n local db
        Optional<Subscriptions> resultFromHTTP = httpRepository.getSubsByNameNotLocally(newString,auth);
        if (resultFromHTTP != null) {
            return resultFromHTTP;
        }

         */
        throw new IllegalArgumentException("Sub of user with name " + newString + " does not exist");
    }
    @Transactional
    public void findIfUserDoesNotHavesSub(String auth,String newString) {

        // local db
        Optional<Subscriptions> resultFromDB = dbRepository.findByActiveStatus_ActiveAndUser(true ,newString);
        if (resultFromDB.isPresent()) {
            throw new IllegalArgumentException("User already haves sub");
        }
        // n local db
        /*
        Optional<Subscriptions> resultFromHTTP = httpRepository.getSubsByNameNotLocally(newString,auth);
        if (resultFromHTTP != null) {
            if (resultFromHTTP.get().getActiveStatus().isActive())
                throw new IllegalArgumentException("User already haves sub");
        }

         */
    }
    @Override
    public void checkIfPlanExist(String name) throws IOException, URISyntaxException, InterruptedException {
        httpRepository.getPlanByName(name);
    }
    public Optional<Subscriptions> findByActiveStatus_ActiveAndUser(boolean b, String user) {
        return dbRepository.findByActiveStatus_ActiveAndUser(b,user);
    }
    public Subscriptions save(Subscriptions obj) {
        return dbRepository.save(obj);
    }

}