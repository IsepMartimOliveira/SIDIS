package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;

import java.util.Optional;

public interface PlansManager {
    void storePlan(PlansDetails planRequest);
    Optional<PlansDetails> findPlan(String plan);
}
