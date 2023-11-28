package com.example.psoft_22_23_project.rabbitMQ;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubsByRabbitRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.UpdateSubsRabbitRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubscriptionsServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SubsQReceiver {
    @Autowired
    private SubscriptionsServiceImpl subsService;

    @RabbitListener(queues = "#{subsQueryQueue.name}")
    public void  createSub(CreateSubsByRabbitRequest subsRequest) {
        subsService.storeSub(subsRequest);
        System.out.println(" [x] Received '" + subsRequest + "' from subs_queue");
    }

    @RabbitListener(queues = "#{sendPlanDetailsQueue.name}")
    public void receivePlanDetails(PlansDetails plansDetails) {
        subsService.notifyAboutReceivedPlanDetails(plansDetails);
        System.out.println(" [x] Received "+plansDetails+" from subs_queue");
    }

    @RabbitListener(queues = "#{updateQueue.name}")
    public void receiveUpdate(UpdateSubsRabbitRequest sub){
        subsService.storePlanUpdate(sub);
        System.out.println(" [x] Received update message '" + sub + "' from subsQ");
    }
    @RabbitListener(queues = "#{cancelQueue.name}")
    public void receiveCancel(UpdateSubsRabbitRequest sub){
        subsService.storeCancel(sub);
        System.out.println(" [x] Received update message '" + sub + "' from subsQ");
    }

    @RabbitListener(queues = "#{renewQueue.name}")
    public void receiveRenew(UpdateSubsRabbitRequest sub){
        subsService.storeRenew(sub);
        System.out.println(" [x] Received update message '" + sub + "' from subsQ");
    }


}
