package com.example.loadbalancer.services;

import com.example.loadbalancer.api.CreatePlanRequest;
import com.example.loadbalancer.model.Plans;
import com.example.loadbalancer.repositories.PlansRepositoryDB;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadBalancerServiceImpl implements LoadBalancerService {

    private  final CreatePlanMapper createPlansMapper;
    private final PlansRepositoryDB plansRepositoryDB;
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
        return null;
    }
}
