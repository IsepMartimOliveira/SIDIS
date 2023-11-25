package com.example.psoft_22_23_project.rabbitMQ;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubsByRabbitRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.UpdateSubsRabbitRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubscriptionsServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class SubsCOMReceiver {
    @Autowired
    private SubscriptionsServiceImpl subscriptionsService;
    @RabbitListener(queues = "#{subQueue.name}")
    public void receiveSub(CreateSubsByRabbitRequest subRequest){
        subscriptionsService.storeSub(subRequest);
        System.out.println(" [x] Received '" + subRequest + "' from sub_queue");
    }

    @RabbitListener(queues = "#{updateQueue.name}")
    public void receiveUpdate(UpdateSubsRabbitRequest sub){
        subscriptionsService.storePlanUpdate(sub);
        System.out.println(" [x] Received update message '" + sub + "' from plansQueue");
    }
    @RabbitListener(queues = "#{cancelQueue.name}")
    public void receiveCancel(UpdateSubsRabbitRequest sub){
        subscriptionsService.storeCancel(sub);
        System.out.println(" [x] Received update message '" + sub + "' from plansQueue");
    }

    @RabbitListener(queues = "#{renewQueue.name}")
    public void receiveRenew(UpdateSubsRabbitRequest sub){
        subscriptionsService.storeRenew(sub);
        System.out.println(" [x] Received update message '" + sub + "' from plansQueue");
    }

}
