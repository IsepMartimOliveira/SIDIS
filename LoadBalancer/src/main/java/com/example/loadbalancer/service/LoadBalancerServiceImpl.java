package com.example.loadbalancer.service;

import com.example.loadbalancer.api.CreatePlanMapper;
import com.example.loadbalancer.api.CreatePlanRequest;
import com.example.loadbalancer.api.EditPlanRequestUpdate;
import com.example.loadbalancer.model.Plans;
import com.example.loadbalancer.repository.LoadBalancerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoadBalancerServiceImpl implements LoadBalanccerService {
    private final  CreatePlanMapper createPlansMapper;
    private final PlansManager plansManager;


    @Override
    public void createPlan(CreatePlanRequest plans) {
        Plans obj=createPlansMapper.create(plans);
        plansManager.save(obj);
    }

    @Override
    public List<CreatePlanRequest> getPlansList() {
        Iterable<Plans> plansList = plansManager.findAll();
        List<CreatePlanRequest> plansDTOList = new ArrayList();
        for (Plans p : plansList) {
            CreatePlanRequest plansDTO = createPlansMapper.createInverse(p);
            plansDTOList.add(plansDTO);
        }
        return plansDTOList;
    }

    public void storePlanUpdate(EditPlanRequestUpdate resource) {
        final Optional<Plans> plansOptional = plansManager.findByNameDoesExists(resource.getName());

        Plans plans=plansOptional.get();
        plans.updateData( resource.getEditPlansRequest().getDescription(),
                resource.getEditPlansRequest().getMaximumNumberOfUsers().toString(),
                resource.getEditPlansRequest().getNumberOfMinutes(),
                resource.getEditPlansRequest().getMusicCollection().toString(),
                resource.getEditPlansRequest().getMusicSuggestion(),
                resource.getEditPlansRequest().getActive().toString(),
                resource.getEditPlansRequest().getPromoted().toString());

        plansManager.save(plans);
    }
}
