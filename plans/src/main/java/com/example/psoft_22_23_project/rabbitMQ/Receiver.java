package com.example.psoft_22_23_project.rabbitMQ;

import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepositoryDB;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @Autowired
    private PlansRepositoryDB plansRepositoryDB;

    @RabbitListener(queues = "plans_Get_One")
    public void consumeMessage(Plans plans) {
        final Plans obj = Plans.newFrom(plans);
        plansRepositoryDB.save(obj);
        System.out.println("Message returned: " + plans);
    }
}