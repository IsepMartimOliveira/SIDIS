package com.example.loadbalancer.service;

import com.example.loadbalancer.api.CreatePlanRequest;
import com.example.loadbalancer.api.CreateSubsByRabbitRequest;
import com.example.loadbalancer.api.CreateSubscriptionsRequest;

import java.util.List;

public interface LoadBalanccerService {

    void createPlan(CreatePlanRequest plans);

    List<CreatePlanRequest> getPlansList();
    List<CreateSubsByRabbitRequest> getSubsList();
}
