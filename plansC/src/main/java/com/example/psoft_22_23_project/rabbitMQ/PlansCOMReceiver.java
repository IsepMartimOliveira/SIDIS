package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PlansCOMReceiver {

    @RabbitListener(queues = "#{plansQueue.name}")
    public void receivePlan(CreatePlanRequest planRequest) {
        System.out.println(" [x] Received '" + planRequest + "' from plans_queue");
    }
}
