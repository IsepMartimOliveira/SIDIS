package com.example.psoft_22_23_project.subscriptionsmanagement.repositories;

import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.PlansManager;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlansManagerImpl implements PlansManager {
    private final PlansRepositoryDB plansRepositoryDB;
    public void storePlan(PlansDetails planRequest) {
        plansRepositoryDB.save(planRequest);
    }

    @Override
    public Optional<PlansDetails> findPlan(String plan) {
        Optional<PlansDetails> plansDetails = plansRepositoryDB.findByName(plan);
        return plansDetails;
    }

}
