package com.example.psoft_22_23_project.rabbitMQ;


import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubsQSender {

    @Autowired
    private AmqpTemplate template;
    private String fanout = "get_plan";
    public void send(String name) {
        template.convertAndSend(fanout, "", name);
        System.out.println(" [x] Get '" + name + "' plan details from subsQ (Sender)");
    }

}
