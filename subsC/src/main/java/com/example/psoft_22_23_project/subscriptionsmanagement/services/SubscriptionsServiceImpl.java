package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.rabbitMQ.SubsCOMSender;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.*;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.sun.tools.jconsole.JConsoleContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class SubscriptionsServiceImpl implements SubscriptionsService {

    private final SubsManager subsManager;
    private final CreateSubscriptionsMapper  createSubscriptionsMapper;
    private final SubsCOMSender subsCOMSender;
    private final SubsByRabbitMapper subsByRabbitMapper;
    private final UpdateSubsByRabbitMapper updateSubsByRabbitMapper;
    private CompletableFuture<Boolean> plansDetailsFuture = new CompletableFuture<>();
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

    @Override
    public Subscriptions create(final CreateSubscriptionsRequest resource) throws URISyntaxException, IOException, InterruptedException, ExecutionException {
        String user = getUsername();
        subsManager.findIfUserDoesNotHavesSub(user);


        //
        plansDetailsFuture = new CompletableFuture<>();
        subsCOMSender.checkPlan(resource.getName());
        Boolean receivedPlansDetails = plansDetailsFuture.get();
        if (!receivedPlansDetails.booleanValue()){
            throw new IOException("Plan with name "+resource.getName()+"does not existe");
        }
        //
        Subscriptions obj = createSubscriptionsMapper.create(user,resource.getName(),resource,false);
        CreateSubsByRabbitRequest rabbitRequest = subsByRabbitMapper.toSubsRabbit(resource,user);
        subsCOMSender.send(rabbitRequest);
        return obj;
    }
    public void storeSub(CreateSubsByRabbitRequest subsRequest) {
        subsManager.findByActiveStatus_ActiveAndUser(true,subsRequest.getUser());
        Subscriptions obj=createSubscriptionsMapper.create(subsRequest.getUser(),subsRequest.getCreateSubscriptionsRequest().getName(),subsRequest.getCreateSubscriptionsRequest(),false);
        subsManager.save(obj);
    }
    @Override
    public Subscriptions cancelSubscription(String auth,final long desiredVersion){
        String user = getUsername();
        Optional<Subscriptions> subscriptions = subsManager.findIfUserHavesSub(auth,user);
        Subscriptions subscriptions1 = subscriptions.get();
        subscriptions1.cancelSubscription(desiredVersion);
        UpdateSubsRabbitRequest updateSubsRabbitRequest = updateSubsByRabbitMapper.toSubsRabbit(auth, desiredVersion,user);
        subsCOMSender.sendCancel(updateSubsRabbitRequest);
        return subscriptions1;
    }
    public void storeCancel(UpdateSubsRabbitRequest sub){
        Optional<Subscriptions> subscriptions = subsManager.findIfUserHavesSub(sub.getAuth(),sub.getUser());
        Subscriptions subscriptions1 = subscriptions.get();
        subscriptions1.cancelSubscription(sub.getDesiredVersion());
        subsManager.save(subscriptions1);
    }

    @Override
    public Subscriptions renewAnualSubscription(String auth,final long desiredVersion) {
        String user = getUsername();
        Optional<Subscriptions> subscriptions = subsManager.findIfUserHavesSub(auth,user);
        Subscriptions subscriptions1 = subscriptions.get();
        subscriptions1.renewSub(desiredVersion);
        UpdateSubsRabbitRequest updateSubsRabbitRequest = updateSubsByRabbitMapper.toSubsRabbit(auth, desiredVersion,user);
        subsCOMSender.sendRenew(updateSubsRabbitRequest);
        return subscriptions1;
    }

    public void storeRenew(UpdateSubsRabbitRequest sub){
        Optional<Subscriptions> subscriptions = subsManager.findIfUserHavesSub(sub.getAuth(),sub.getUser());
        Subscriptions subscriptions1 = subscriptions.get();
        subscriptions1.renewSub(sub.getDesiredVersion());
        subsManager.save(subscriptions1);
    }
    @Override
    public Subscriptions changePlan(final long desiredVersion, final String name,final String auth) throws IOException, URISyntaxException, InterruptedException, ExecutionException {
        String user = getUsername();
        Optional<Subscriptions> subscriptions = subsManager.findIfUserHavesSub(auth,user);
        //
        plansDetailsFuture = new CompletableFuture<>();
        subsCOMSender.checkPlan(name);
        Boolean receivedPlansDetails = plansDetailsFuture.get();
        if (!receivedPlansDetails.booleanValue()){
            throw new IOException("Plan with name "+name+"does not existe");
        }
        //
        Subscriptions subscriptions1 = subscriptions.get();
        subscriptions1.changePlan(desiredVersion,name);
        UpdateSubsRabbitRequest updateSubsRabbitRequest = updateSubsByRabbitMapper.toSubsRabbit(auth, desiredVersion,user,name);
        subsCOMSender.sendUpdatePlan(updateSubsRabbitRequest);
        return subscriptions1;
    }
    public void storePlanUpdate(UpdateSubsRabbitRequest sub){
        Optional<Subscriptions> subscriptions = subsManager.findIfUserHavesSub(sub.getAuth(),sub.getUser());
        Subscriptions subscriptions1 = subscriptions.get();
        subscriptions1.changePlan(sub.getDesiredVersion(),sub.getName());
        subsManager.save(subscriptions1);
    }
    public void notifyAboutReceivedPlanDetails(boolean b) {
        plansDetailsFuture.complete(b);
    }

    @Transactional
    public void createBonusPlan(CreatePlanRequestBonus plansBonusName) {
        String user = plansBonusName.getUsername();

        try {
            Optional<Subscriptions> subscription = subsManager.findByUser(user);

            if (subscription.isPresent()) {
                Subscriptions subscriptions2 = subscription.get();

                if (!subscriptions2.getIsBonus()) {
                    subscriptions2.changeBonus(true, plansBonusName.getName());
                    subsManager.save(subscriptions2);
                   // UpdateSubsRabbitRequest updateSubsRabbitRequest = updateSubsByRabbitMapper.toSubsRabbit("ads", subscriptions2.getVersion(),user,plansBonusName.getName());
                    //subsCOMSender.sendUpdatePlan(updateSubsRabbitRequest);
                }else{
                    throw new IllegalArgumentException("ALREADY HAVE SUB");
                }
            } else {
                Subscriptions obj = createSubscriptionsMapper.createBonus(user, plansBonusName.getName(), plansBonusName, true);
                subsManager.save(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
