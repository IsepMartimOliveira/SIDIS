package com.example.loadbalancer.service;

import com.example.loadbalancer.api.CreatePlanRequest;

import java.util.List;

public interface LoadBalanccerService {

    void createPlan(CreatePlanRequest plans);

    List<CreatePlanRequest> getPlansList();
}
