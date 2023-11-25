package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import com.example.psoft_22_23_project.plansmanagement.services.PlansServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlansQReceiver {
    @Autowired
    private PlansServiceImpl plansService;

    @RabbitListener(queues = "#{plansQueryQueue.name}")
    public void receivePlan(CreatePlanRequest planRequest) {
        plansService.storePlan(planRequest);

        System.out.println(" [x] Received '" + planRequest + "' from plans_queue");
    }
}
