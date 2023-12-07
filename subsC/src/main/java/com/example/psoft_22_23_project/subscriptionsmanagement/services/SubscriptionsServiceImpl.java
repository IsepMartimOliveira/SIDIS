package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.rabbitMQ.SubsCOMSender;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.*;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    public Subscriptions create(final CreateSubscriptionsRequest resource,String auth) throws URISyntaxException, IOException, InterruptedException, ExecutionException {
        String user = getUsername();
        subsManager.findIfUserDoesNotHavesSub(auth,user);
        //
        plansDetailsFuture = new CompletableFuture<>();
        subsCOMSender.checkPlan(resource.getName());
        Boolean receivedPlansDetails = plansDetailsFuture.get();
        if (!receivedPlansDetails.booleanValue()){
            throw new IOException("Plan with name "+resource.getName()+"does not existe");
        }
        //
        Subscriptions obj = createSubscriptionsMapper.create(user,resource.getName(),resource);
        CreateSubsByRabbitRequest rabbitRequest = subsByRabbitMapper.toSubsRabbit(resource,user);
        subsCOMSender.send(rabbitRequest);
        return obj;
    }
    public void storeSub(CreateSubsByRabbitRequest subsRequest) {
        subsManager.findByActiveStatus_ActiveAndUser(true,subsRequest.getUser());
        Subscriptions obj=createSubscriptionsMapper.create(subsRequest.getUser(),subsRequest.getCreateSubscriptionsRequest().getName(),subsRequest.getCreateSubscriptionsRequest());
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

}
