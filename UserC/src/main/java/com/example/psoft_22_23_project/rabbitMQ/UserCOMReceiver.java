package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.usermanagement.services.CreateUserRequest;
import com.example.psoft_22_23_project.usermanagement.services.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCOMReceiver {
    @Autowired
    UserService userService;
    @RabbitListener(queues = "#{userQueue.name}")
    public void receivePlan(CreateUserRequest userRequest){
        userService.storeUser(userRequest);
        System.out.println(" [x] Received '" + userRequest + "' from user_queue");
    }
}
