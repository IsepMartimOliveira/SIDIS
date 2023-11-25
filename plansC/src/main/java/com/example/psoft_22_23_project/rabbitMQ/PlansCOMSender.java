package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import com.example.psoft_22_23_project.plansmanagement.api.EditPlanRequestUpdate;
import com.example.psoft_22_23_project.plansmanagement.api.EditPlansRequest;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlansCOMSender {

    @Autowired
    private AmqpTemplate template;

    private String fanout = "plans_create";
    private String fanoutDelete = "plans_to_delete";

    private String fanoutUpdate="plans_to_update";
    public void send(CreatePlanRequest planRequest) {
        template.convertAndSend(fanout, "", planRequest);

        System.out.println(" [x] Sent '" + planRequest + "' to create a plan");
    }
    public void sendUpdate(EditPlanRequestUpdate updatedPlans) {
        template.convertAndSend(fanoutUpdate, "", updatedPlans);
        System.out.println(" [x] Sent '" + updatedPlans + "' to update a plan");

    }

}
