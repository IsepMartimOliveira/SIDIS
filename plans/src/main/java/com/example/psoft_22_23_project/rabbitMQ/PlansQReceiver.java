package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import com.example.psoft_22_23_project.plansmanagement.api.DeactivatPlanRequest;
import com.example.psoft_22_23_project.plansmanagement.api.EditPlanRequestUpdate;
import com.example.psoft_22_23_project.plansmanagement.services.PlansManager;
import com.example.psoft_22_23_project.plansmanagement.services.PlansServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlansQReceiver {
    @Autowired
    private PlansServiceImpl plansService;
    private PlansManager plansManager;

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
}
