package com.example.loadbalancer.service;

import com.example.loadbalancer.api.*;
import com.example.loadbalancer.model.Plans;
import com.example.loadbalancer.model.Subscriptions;
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
    private final  CreateSubscriptionsMapper createSubscriptionsMapper;
    private final PlansManager plansManager;
    private final SubsManager subsManager;


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

    public  void  storePlanDeactivates(DeactivatPlanRequest deactivatPlanRequest){
        final Optional<Plans> plansOptional = plansManager.findByNameDoesExists(deactivatPlanRequest.getName());
        Plans plans=plansOptional.get();
        plans.deactivate();
        plansManager.save(plans);
    }

    public void storeBonusPlan(CreatePlanRequestBonus resource){
        plansManager.findByNameDoesNotExists(resource.getName());
        Plans obj = createPlansMapper.createBonus(resource);
        plansManager.save(obj);
    }

    public void deleteBonus(String plansBonus) {plansManager.deleteByName(plansBonus);}

    public void storeSub(CreateSubsByRabbitRequest subsRequest) {
        subsManager.findByActiveStatus_ActiveAndUser("true",subsRequest.getUser());
        Subscriptions obj=createSubscriptionsMapper.create(subsRequest.getUser(),subsRequest.getCreateSubscriptionsRequest().getName(),subsRequest.getCreateSubscriptionsRequest(),false);
        subsManager.save(obj);
    }

    @Override
    public List<CreateSubsByRabbitRequest> getSubsList() {
        Iterable<Subscriptions> subsList = subsManager.findAll();
        List<CreateSubsByRabbitRequest> subsDTOList = new ArrayList<>();
        for (Subscriptions subscription : subsList) {
            String user = subscription.getUser();
            CreateSubsByRabbitRequest subsDTO = createSubscriptionsMapper.createInverse(subscription, user);
            subsDTOList.add(subsDTO);
        }
        return subsDTOList;
    }

    public void changeSub(UpdateSubsRabbitRequest sub) {
        try {
            Optional<Subscriptions> subscriptions = subsManager.findByActiveStatus_ActiveAndUser("true",sub.getUser());
            Subscriptions subscriptions1 = subscriptions.get();
            subscriptions1.changePlan(sub.getName());
            subsManager.save(subscriptions1);}
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void storeSubBonus(CreateSubsByRabbitRequest subsRequest) {
        try {
            subsManager.findByActiveStatus_ActiveAndUser("true",subsRequest.getUser());
            Subscriptions obj=createSubscriptionsMapper.create(subsRequest.getUser(),subsRequest.getCreateSubscriptionsRequest().getName(),subsRequest.getCreateSubscriptionsRequest(),true);
            subsManager.save(obj);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void storePlanUpdateBonus(UpdateSubsRabbitRequest sub) {
        try {
            Optional<Subscriptions> subscriptions = subsManager.findByUser(sub.getUser());
            Subscriptions subscriptions1 = subscriptions.get();
            subscriptions1.changePlan(sub.getName());
            subsManager.save(subscriptions1);}
        catch (Exception e){
            e.printStackTrace();
        }
    }


}
