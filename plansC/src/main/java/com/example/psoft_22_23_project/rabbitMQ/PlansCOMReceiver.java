package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import com.example.psoft_22_23_project.plansmanagement.api.EditPlanRequestUpdate;
import com.example.psoft_22_23_project.plansmanagement.api.EditPlansRequest;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.services.PlansServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlansCOMReceiver {
    @Autowired
    private  PlansServiceImpl plansService;
    @RabbitListener(queues = "#{plansQueue.name}")
    public void receivePlan(CreatePlanRequest planRequest){
        plansService.storePlan(planRequest);
        System.out.println(" [x] Received '" + planRequest + "' from plans_queue");
    }

    @RabbitListener(queues = "#{updateQueue.name}")
    public void receiveUpdate(EditPlanRequestUpdate plans) {
        plansService.storePlanUpdate(plans);
        System.out.println(" [x] Received update message '" + plans + "' from plansQueue");
    }
}
