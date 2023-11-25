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
    public FanoutExchange deleteFanout() {
        return new FanoutExchange("plans_to_delete");
    }
    @Bean
    public FanoutExchange updateFanout() {
        return new FanoutExchange("plans_to_update");
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
    public Queue updateQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding bindingUpdateQueue(FanoutExchange updateFanout, Queue updateQueue) {
        return BindingBuilder.bind(updateQueue).to(updateFanout);
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


}