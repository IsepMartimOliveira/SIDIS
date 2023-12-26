package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequestBonus;
import com.example.psoft_22_23_project.plansmanagement.api.DeactivatPlanRequest;
import com.example.psoft_22_23_project.plansmanagement.api.EditPlanRequestUpdate;
import com.example.psoft_22_23_project.plansmanagement.services.PlansManager;
import com.example.psoft_22_23_project.plansmanagement.services.PlansServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class PlansQReceiver {
    @Autowired
    private PlansServiceImpl plansService;
    @RabbitListener(queues = "#{plansQueryQueue.name}")
    public void receivePlan(CreatePlanRequest planRequest) {
        plansService.storePlan(planRequest);

        System.out.println(" [x] Received '" + planRequest + "' from plans_queue");
    }
    @RabbitListener(queues = "#{updateQueue.name}")
    public void receiveUpdate(EditPlanRequestUpdate plans) {
        plansService.storePlanUpdate(plans);
        System.out.println(" [x] Received update message '" + plans + "' from plansQueue");
    }
    @RabbitListener(queues = "#{deactivateQueue.name}")
    public void receiveDeactivate(DeactivatPlanRequest plans) {
        plansService.storePlanDeactivates(plans);
        System.out.println(" [x] Received update message '" + plans + "' from plansQueue");
    }

    @RabbitListener(queues = "#{getPlanNameQueue.name}")
    public void getPlanDetails(String name){
        plansService.getPlanDetails(name);
        System.out.println(" [x] Received message to Get plan:'" + name + " 'details from subsQ ");
    }
    @RabbitListener(queues = "#{checkPlanQueue.name}")
    public void receiveCheckPlan(String planName) {
        plansService.checkPlan(planName);
        System.out.println(" [x] Receive msg to check plan with name: '" + planName );
    }
    @RabbitListener(queues = "#{planBonusQueue.name}")
    public void receivePlanBonus(CreatePlanRequestBonus plansBonus){
        plansService.storeBonusPlan(plansBonus);
        System.out.println(" [x] Received deactivate message '" + plansBonus + "' from planBonusQueue");

    }

}
