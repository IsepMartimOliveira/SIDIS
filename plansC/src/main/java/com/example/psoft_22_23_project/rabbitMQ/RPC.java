package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import com.example.psoft_22_23_project.plansmanagement.services.PlansServiceImpl;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class RPC {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private PlansServiceImpl plansService;
    public void helper() {
        List<CreatePlanRequest> plansList = (List<CreatePlanRequest>) amqpTemplate.convertSendAndReceive("rpc_plans", "key", "");
        if (plansList != null) {

            for (CreatePlanRequest p : plansList) {
                plansService.storePlan(p);
            }
        } else {

        }

    }
}
