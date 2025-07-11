package com.example.psoft_22_23_project.subscriptionsmanagement.repositories;

import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreatePlanRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.PlansDetailsMapper;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubsManagerImpl implements SubsManager {
    private final SubscriptionsRepositoryDB dbRepository;
    @Transactional
    public Optional<Subscriptions> findSub(String name) {
        // local db
        Optional<Subscriptions> resultFromDB = dbRepository.findByActiveStatus_ActiveAndUser(true ,name);
        if (resultFromDB.isPresent()) {
            return resultFromDB;
        }
        throw new IllegalArgumentException("Sub of user with name " + name + " does not exist");
    }
    @Transactional
    public Optional<Subscriptions> findSubBonus(String name) {
        Optional<Subscriptions> resultFromDB = dbRepository.findByActiveStatus_ActiveAndUser(true ,name);
        return resultFromDB;

    }

    public Optional<Subscriptions> findByUser(String name){
        Optional<Subscriptions> resultFromDB = dbRepository.findByUser(name);
        return resultFromDB;
    }

    public Optional<Subscriptions> findByActiveStatus_ActiveAndUser(boolean b, String user) {
        return dbRepository.findByActiveStatus_ActiveAndUser(b,user);
    }
    public Subscriptions save(Subscriptions obj) {
        return dbRepository.save(obj);
    }

}
