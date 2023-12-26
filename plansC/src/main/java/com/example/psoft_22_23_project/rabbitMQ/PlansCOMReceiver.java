package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.plansmanagement.api.*;
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
        System.out.println(" [x] Received update message '" + plans + "' from updateQueue");
    }
    @RabbitListener(queues = "#{deactivateQueue.name}")
    public void receiveDeactivate(DeactivatPlanRequest plans) {
        plansService.storePlanDeactivates(plans);
        System.out.println(" [x] Received deactivate message '" + plans + "' from deactivateQueue");
    }

    @RabbitListener(queues = "#{planBonusQueue.name}")
    public void receivePlanBonus(CreatePlanRequestBonus plansBonus){
        plansService.storeBonusPlan(plansBonus);
        System.out.println(" [x] Received bonus message '" + plansBonus + "' from planBonusQueue");

    }
}
