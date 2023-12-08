package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.Subs;
import com.example.psoft_22_23_project.rabbitMQ.SubsQSender;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.*;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class SubscriptionsServiceImpl implements SubscriptionsService {

    private final SubsManager subsManager;
    private final CreateSubscriptionsMapper createSubscriptionsMapper;
    private final PlansDetailsMapper plansDetailsMapper;

    @SneakyThrows
    @Override
    public Optional<PlansDetails> planDetails(String auth) {
        String name = getUsername();
        Optional<Subscriptions> subscription = subsManager.findSub(auth,name);
        Optional<PlansDetails> plansDetails = subsManager.findPlan(subscription.get().getPlan());
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
        Subscriptions obj=createSubscriptionsMapper.create(subsRequest.getUser(),subsRequest.getCreateSubscriptionsRequest().getName(),subsRequest);
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
        subsManager.storePlan(plansDetail);
    }

    public void storePlanEdit(EditPlanRequestUpdate resource) {
        final Optional<PlansDetails> plansOptional = subsManager.findPlan(resource.getName());

        PlansDetails plans=plansOptional.get();
        plans.updateData(resource.getDesiredVersion(), resource.getEditPlansRequest().getDescription(),
                resource.getEditPlansRequest().getMaximumNumberOfUsers(),
                resource.getEditPlansRequest().getNumberOfMinutes(),
                resource.getEditPlansRequest().getMusicCollection(),
                resource.getEditPlansRequest().getMusicSuggestion(),
                resource.getEditPlansRequest().getActive(),
                resource.getEditPlansRequest().getPromoted());

        subsManager.storePlan(plans);
    }
    public  void  storePlanDeactivates(DeactivatPlanRequest deactivatPlanRequest){
        final Optional<PlansDetails> plansOptional = subsManager.findPlan(deactivatPlanRequest.getName());
        PlansDetails plans=plansOptional.get();
        plans.deactivate(deactivatPlanRequest.getDesiredVersion());
        subsManager.storePlan(plans);
    }
}






