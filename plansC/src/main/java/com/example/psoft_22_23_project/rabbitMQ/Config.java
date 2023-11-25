package com.example.psoft_22_23_project.rabbitMQ;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange("plans_create");
    }
    @Bean
    public Queue plansQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Queue planQ2() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue planC2() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue planQ() {
        return new AnonymousQueue();
    }
    @Bean
    public Binding bindingPlansQueue(FanoutExchange fanout, Queue plansQueue) {
        return BindingBuilder.bind(plansQueue).to(fanout);
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public PlansCOMSender sender() {
        return new PlansCOMSender();
    }
    @Bean
    public PlansCOMReceiver receiver(){
        return new PlansCOMReceiver();
    }


    @Bean
    public Binding bindingPlanQ2(FanoutExchange fanout, Queue planQ2) {
        return BindingBuilder.bind(planQ2).to(fanout);
    }

    @Bean
    public Binding bindingPlanC2(FanoutExchange fanout, Queue planC2) {
        return BindingBuilder.bind(planC2).to(fanout);
    }

    @Bean
    public Binding bindingPlanQ(FanoutExchange fanout, Queue planQ) {
        return BindingBuilder.bind(planQ).to(fanout);
    }
}