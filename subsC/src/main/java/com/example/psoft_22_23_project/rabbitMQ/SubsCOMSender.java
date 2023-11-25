package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubsCOMSender {

    @Autowired
    private AmqpTemplate template;

    private String fanout = "sub_create";
    private String fanoutDelete = "sub_to_delete";

    private String fanoutUpdate="sub_to_update";
    public void send(CreateSubscriptionsRequest subRequest) {
        template.convertAndSend(fanout, "", subRequest);
        System.out.println(" [x] Sent '" + subRequest + "' to create a sub");
    }
    public void sendUpdate(String updatedSub) {
        template.convertAndSend(fanoutUpdate, "", updatedSub);
        System.out.println(" [x] Sent '" + updatedSub + "' to update a sub");

    }

}
