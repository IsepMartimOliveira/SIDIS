package com.example.loadbalancer.services;

import com.example.loadbalancer.api.CreatePlanRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface LoadBalancerService {
     void createPlan(CreatePlanRequest plans);

    List<CreatePlanRequest> getPlansList();
}
