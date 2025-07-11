package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.usermanagement.services.CreateUserRequest;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCOMSender {

    @Autowired
    private AmqpTemplate template;
    private String fanout="user_create";
    public void send(CreateUserRequest userRequest) {
        template.convertAndSend(fanout, "", userRequest);

        System.out.println(" [x] Sent '" + userRequest + "' to create a user");
    }


}
