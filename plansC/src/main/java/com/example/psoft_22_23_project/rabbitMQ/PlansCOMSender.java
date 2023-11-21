package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import com.example.psoft_22_23_project.plansmanagement.api.PlanRequest;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlansCOMSender {

    @Autowired
    private AmqpTemplate template;

    private String fanout = "plans_create";
    private String plansQueue = "plans_queue";

    public void send(CreatePlanRequest planRequest) {
        // Send the message to the fanout exchange
        template.convertAndSend(fanout, "", planRequest);

        // Also send the message to the specific queue
        template.convertAndSend(fanout, plansQueue, planRequest);

        System.out.println(" [x] Sent '" + planRequest + "' to both the fanout exchange and plans_queue");
    }
}
