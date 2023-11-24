package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.services.CreatePlansMapperImpl;
import com.example.psoft_22_23_project.plansmanagement.services.PlansServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class PlansCOMReceiver {
    PlansServiceImpl plansService;
    @RabbitListener(queues = "#{plansQueue.name}")
    public void receivePlan(CreatePlanRequest planRequest) throws URISyntaxException, IOException, InterruptedException {
        plansService.create(planRequest);
        System.out.println(" [x] Received '" + planRequest + "' from plans_queue");
    }
}
