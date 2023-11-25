package com.example.psoft_22_23_project.rabbitMQ;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubscriptionsServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubsQReceiver {
    @Autowired
    private SubscriptionsServiceImpl subsService;

    @RabbitListener(queues = "#{subsQueryQueue.name}")
    public void receivePlan(CreateSubscriptionsRequest subsRequest) {
        subsService.storeSub(subsRequest);
        System.out.println(" [x] Received '" + subsRequest + "' from subs_queue");
    }
}
