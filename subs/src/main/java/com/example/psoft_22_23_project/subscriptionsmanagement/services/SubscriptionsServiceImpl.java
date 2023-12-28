package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.Subs;
import com.example.psoft_22_23_project.exceptions.NotFoundException;
import com.example.psoft_22_23_project.rabbitMQ.SubsQSender;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.*;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.repositories.PlansManagerImpl;
import com.example.psoft_22_23_project.subscriptionsmanagement.repositories.SubsManagerImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class SubscriptionsServiceImpl implements SubscriptionsService {

    private final SubsManagerImpl subsManager;
    private final PlansManagerImpl plansManager;
    private final CreateSubscriptionsMapper createSubscriptionsMapper;
    private final PlansDetailsMapper plansDetailsMapper;

    @SneakyThrows
    @Override
    public Optional<PlansDetails> planDetails(String auth) {
        String name = getUsername();
        Optional<Subscriptions> subscription = subsManager.findSub(name);
        Optional<PlansDetails> plansDetails = plansManager.findPlan(subscription.get().getPlan());
        return plansDetails;
    }
    private String getUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int commaIndex = username.indexOf(",");
        String newString;
        if (commaIndex != -1) {
            newString = username.substring(commaIndex + 1);
        } else {
            newString = username;
        }
        return  newString;
    }
    public void storeSub(CreateSubsByRabbitRequest subsRequest) {
        subsManager.findByActiveStatus_ActiveAndUser(true,subsRequest.getUser());
        Subscriptions obj=createSubscriptionsMapper.create(subsRequest.getUser(),subsRequest.getCreateSubscriptionsRequest().getName(),subsRequest,false);
        subsManager.save(obj);
    }

    public void storePlanUpdate(UpdateSubsRabbitRequest sub){

        Optional<Subscriptions> subscriptions = subsManager.findByActiveStatus_ActiveAndUser(true,sub.getUser());
        Subscriptions subscriptions1 = subscriptions.get();
        subscriptions1.changePlan(sub.getDesiredVersion(),sub.getName());
        subsManager.save(subscriptions1);

    }

    public void storeRenew(UpdateSubsRabbitRequest sub){
        Optional<Subscriptions> subscriptions = subsManager.findByActiveStatus_ActiveAndUser(true,sub.getUser());
        Subscriptions subscriptions1 = subscriptions.get();
        subscriptions1.renewSub(sub.getDesiredVersion());
        subsManager.save(subscriptions1);

    }
    public void storeCancel(UpdateSubsRabbitRequest sub){
        Optional<Subscriptions> subscriptions = subsManager.findByActiveStatus_ActiveAndUser(true,sub.getUser());
        Subscriptions subscriptions1 = subscriptions.get();
        subscriptions1.cancelSubscription(sub.getDesiredVersion());
        subsManager.save(subscriptions1);
    }

    public void storePlan(CreatePlanRequest planRequest) {
        PlansDetails plansDetail = plansDetailsMapper.toPlansDetails(planRequest);
        plansManager.storePlan(plansDetail);
    }
    public void updatePlan(EditPlanRequestUpdate planRequestUpdate){
        PlansDetails existingPlanDetails = plansManager.findPlan(planRequestUpdate.getName())
                .orElseThrow(() -> new NotFoundException("PlanDetails with name " + planRequestUpdate.getName() + " not found"));

        EditPlansRequest editPlansRequest = planRequestUpdate.getEditPlansRequest();
        String description = editPlansRequest.getDescription();
        String numberOfMinutes = editPlansRequest.getNumberOfMinutes();
        String maximumNumberOfUsers = String.valueOf(editPlansRequest.getMaximumNumberOfUsers());
        String musicCollection = String.valueOf(editPlansRequest.getMusicCollection());
        String musicSuggestion = editPlansRequest.getMusicSuggestion();
        Boolean active = editPlansRequest.getActive();
        Boolean promoted = editPlansRequest.getPromoted();

        existingPlanDetails.updateDetails(description, numberOfMinutes, maximumNumberOfUsers,
                musicCollection, musicSuggestion, null, null, active.toString(), promoted.toString());

        plansManager.storePlan(existingPlanDetails);

    }
    public void storePlanDeactivates(DeactivatPlanRequest plans) {
        PlansDetails existingPlanDetails = plansManager.findPlan(plans.getName())
                .orElseThrow(() -> new NotFoundException("PlanDetails with name " + plans.getName() + " not found"));
            existingPlanDetails.deactivate(plans.getDesiredVersion());
        plansManager.storePlan(existingPlanDetails);
    }

    public void storePlanUpdateBonus(UpdateSubsRabbitRequest sub) {
        try {
        Optional<Subscriptions> subscriptions = subsManager.findByActiveStatus_ActiveAndUser(true,sub.getUser());
        Subscriptions subscriptions1 = subscriptions.get();
        subscriptions1.changePlan(sub.getDesiredVersion(),sub.getName());
        subsManager.save(subscriptions1);}
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void storeSubBonus(CreateSubsByRabbitRequest subsRequest) {
        try {
            subsManager.findByActiveStatus_ActiveAndUser(true,subsRequest.getUser());
            Subscriptions obj=createSubscriptionsMapper.create(subsRequest.getUser(),subsRequest.getCreateSubscriptionsRequest().getName(),subsRequest,true);
            subsManager.save(obj);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}






