package com.example.loadbalancer.service;

import com.example.loadbalancer.api.CreatePlanMapper;
import com.example.loadbalancer.api.CreatePlanRequest;
import com.example.loadbalancer.model.Plans;
import com.example.loadbalancer.repository.LoadBalancerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class LoadBalancerServiceImpl implements LoadBalanccerService {
    private final  CreatePlanMapper createPlansMapper;
    private final LoadBalancerRepository plansRepositoryDB;

    @Override
    public void createPlan(CreatePlanRequest plans) {
        Plans obj=createPlansMapper.create(plans);
        plansRepositoryDB.save(obj);
    }

    @Override
    public List<CreatePlanRequest> getPlansList() {
        Iterable<Plans> plansList = plansRepositoryDB.findAll();
        List<CreatePlanRequest> plansDTOList = new ArrayList();
        for (Plans p : plansList) {
            CreatePlanRequest plansDTO = createPlansMapper.createInverse(p);
            plansDTOList.add(plansDTO);
        }
        return plansDTOList;
    }
}
