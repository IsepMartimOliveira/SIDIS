package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.plansmanagement.api.*;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlansCOMSender {

    @Autowired
    private AmqpTemplate template;

    private String fanout = "plans_create";
    private String fanoutDeactivate = "plans_to_deactivate";

    private String bonus_plan="create_plan_bonus";

    private String fanoutUpdate="plans_to_update";


    public void send(CreatePlanRequest planRequest) {
        template.convertAndSend(fanout, "", planRequest);

        System.out.println(" [x] Sent '" + planRequest + "' to create a plan");
    }
    public void sendUpdate(EditPlanRequestUpdate updatedPlans) {
        template.convertAndSend(fanoutUpdate, "", updatedPlans);
        System.out.println(" [x] Sent '" + updatedPlans + "' to update a plan");

    }
    public void sendDeactivate(DeactivatPlanRequest deactivatPlanRequest){
        template.convertAndSend(fanoutDeactivate, "", deactivatPlanRequest);

    }

    public void sendBonusBroadcast(CreatePlanRequestBonus planBonusName){
        template.convertAndSend(bonus_plan, "", planBonusName);
        System.out.println(" [x] Sent '" + planBonusName + "' to create a bonus plan");

    }
    public void sendBonusToSubC(CreatePlanRequestBonus planBonusName){
        template.convertAndSend("plan_to_sub", "key2", planBonusName);
        System.out.println(" [x] Sent '" + planBonusName + "' to create a bonus plan");

    }

}
