package com.example.psoft_22_23_project.rabbitMQ;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue plansGetOneQueue() {
        // Declare a queue named 'plans_Get_One' with specific properties
        return new Queue("plans_queue", false, false, false);
    }

}