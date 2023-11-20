package com.example.psoft_22_23_project.rabbitMQ;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue plansGetOneQueue() {
        return new Queue("plans_Get_One", false, false, false);
    }
}