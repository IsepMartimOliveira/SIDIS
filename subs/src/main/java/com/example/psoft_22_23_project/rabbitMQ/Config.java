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
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange("subs_create");
    }
    @Bean
    public Queue subsQueryQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding bindingPlansQueryQueue(FanoutExchange fanout, Queue subsQueryQueue) {
        return BindingBuilder.bind(subsQueryQueue).to(fanout);
    }
    @Bean
    public  SubsQReceiver receiver(){
        return new SubsQReceiver();
    }
}