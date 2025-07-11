package com.example.psoft_22_23_project.rabbitMQ;


import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubsQSender {

    @Autowired
    private AmqpTemplate template;


}
