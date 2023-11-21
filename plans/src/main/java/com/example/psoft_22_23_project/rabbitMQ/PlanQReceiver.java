package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import com.example.psoft_22_23_project.plansmanagement.api.PlanRequest;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepositoryDB;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlanQReceiver {

    @RabbitListener(queues = "plans_queue")
    public void receiveGetPlanCommand(CreatePlanRequest planRequest) {
        System.out.println("Received GET request for plan: " + planRequest.getName());
    }
}