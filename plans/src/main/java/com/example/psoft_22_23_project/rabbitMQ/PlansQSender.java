package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.plansmanagement.model.PlansDetails;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlansQSender {

    @Autowired
    private AmqpTemplate template;

    private String fanoutSendPlan="send_plan_detail";
    private String fanoutCheckPlan="send_check_plan";

    public void sendPlanDetails(PlansDetails plansDetails) {
        template.convertAndSend(fanoutSendPlan, "", plansDetails);
        System.out.println(" [x] Sent '" + plansDetails + "' plan details");
    }
    public void sendPlanCheck(Boolean b) {
        template.convertAndSend(fanoutCheckPlan, "", b);
        System.out.println(" [x] Sent '" + b + "' plan details");
    }

}
