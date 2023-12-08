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
class SubsManagerImpl implements SubsManager {
    private final SubscriptionsRepositoryDB dbRepository;
    private final PlansRepositoryDB plansRepositoryDB;
    @Transactional
    public Optional<Subscriptions> findSub( String auth,String name) {
        // local db
        Optional<Subscriptions> resultFromDB = dbRepository.findByActiveStatus_ActiveAndUser(true ,name);
        if (resultFromDB.isPresent()) {
            return resultFromDB;
        }
        throw new IllegalArgumentException("Sub of user with name " + name + " does not exist");
    }

    public Optional<Subscriptions> findByActiveStatus_ActiveAndUser(boolean b, String user) {
        return dbRepository.findByActiveStatus_ActiveAndUser(b,user);
    }
    public Subscriptions save(Subscriptions obj) {
        return dbRepository.save(obj);
    }

    public void storePlan(PlansDetails planRequest) {
        plansRepositoryDB.save(planRequest);
    }

    @Override
    public Optional<PlansDetails> findPlan(String plan) {
        Optional<PlansDetails> plansDetails = plansRepositoryDB.findByName(plan);
        return plansDetails;
    }



}
