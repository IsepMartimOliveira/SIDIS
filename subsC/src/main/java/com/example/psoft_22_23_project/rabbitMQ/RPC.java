package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubsByRabbitRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubscriptionsServiceImpl;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RPC {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private SubscriptionsServiceImpl subsService;
    public void helper() {
        List<CreateSubsByRabbitRequest> subsList = (List<CreateSubsByRabbitRequest>) amqpTemplate.convertSendAndReceive("rpc_subs", "key", "");
        if (subsList != null) {

            for (CreateSubsByRabbitRequest p : subsList) {
                subsService.storeSub(p);
            }
        } else {

        }

    }
}
