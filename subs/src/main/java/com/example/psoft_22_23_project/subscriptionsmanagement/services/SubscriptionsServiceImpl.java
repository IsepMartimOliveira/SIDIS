package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.Subs;
import com.example.psoft_22_23_project.rabbitMQ.SubsQSender;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubsByRabbitRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.SubsByRabbitMapper;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionsServiceImpl implements SubscriptionsService {

    private final SubsManager subsManager;
    private final CreateSubscriptionsMapper createSubscriptionsMapper;
    private final SubsByRabbitMapper subsByRabbitMapper;

    private final SubsQSender sender;
    private PlansDetails plansDetails = new PlansDetails();

    @SneakyThrows
    @Override
    public Optional<PlansDetails> planDetails(String auth) {
        String name = getUsername();
        Optional<Subscriptions> subscription = subsManager.findSub(auth,name);
        sender.send(subscription.get().getPlan());
        return Optional.ofNullable(plansDetails);

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


    public void createPlanDetail(PlansDetails plansDetailss) {
        this.plansDetails = plansDetailss;

    }
}






