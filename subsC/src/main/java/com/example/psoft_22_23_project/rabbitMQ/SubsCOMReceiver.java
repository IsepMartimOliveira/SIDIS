package com.example.psoft_22_23_project.rabbitMQ;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubscriptionsServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubsCOMReceiver {
    @Autowired
    private SubscriptionsServiceImpl subscriptionsService;
    @RabbitListener(queues = "#{subQueue.name}")
    public void receiveSub(CreateSubscriptionsRequest subRequest){
        subscriptionsService.storeSub(subRequest);
        System.out.println(" [x] Received '" + subRequest + "' from sub_queue");
    }
/*
    @RabbitListener(queues = "#{updateQueue.name}")
    public void receiveUpdate(EditPlanRequestUpdate plans) {
        plansService.storePlanUpdate(plans);
        System.out.println(" [x] Received update message '" + plans + "' from plansQueue");
    }

 */
}
