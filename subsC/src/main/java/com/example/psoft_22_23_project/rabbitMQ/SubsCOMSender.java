package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubsByRabbitRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.UpdateSubsRabbitRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubsCOMSender {

    @Autowired
    private AmqpTemplate template;

    private String fanout = "sub_to_create";
    private String fanoutUpdate="sub_to_update";
    private String fanoutRenew="sub_to_renew";
    private String fanoutCancel="sub_to_cancel";

    public void send(CreateSubsByRabbitRequest subRequest) {
        template.convertAndSend(fanout, "", subRequest);
        System.out.println(" [x] Sent '" + subRequest + "' to create a sub");
    }
    public void sendUpdatePlan(UpdateSubsRabbitRequest updatedSub) {
        template.convertAndSend(fanoutUpdate, "", updatedSub);
        System.out.println(" [x] Sent '" + updatedSub + "' to update a sub");
    }
    public void sendRenew(UpdateSubsRabbitRequest updatedSub) {
        template.convertAndSend(fanoutRenew, "", updatedSub);
        System.out.println(" [x] Sent '" + updatedSub + "' to update a sub");
    }
    public void sendCancel(UpdateSubsRabbitRequest updatedSub) {
        template.convertAndSend(fanoutCancel, "", updatedSub);
        System.out.println(" [x] Sent '" + updatedSub + "' to update a sub");
    }


}
