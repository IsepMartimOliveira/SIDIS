package com.example.loadbalancer.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
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
        return new FanoutExchange("plans_create");
    }
    @Bean
    public Queue createPlanQueueBalancer(){return new AnonymousQueue();}

    @Bean
    public Queue queuePlansRPC(){
        return new Queue("rpc_plans_receiver");
    }
    @Bean
    public DirectExchange plansExchange(){
        return new DirectExchange("rpc_plans");
    }
    @Bean
    public Binding bindingReceiver(DirectExchange plansExchange, Queue queuePlansRPC){
        return BindingBuilder.bind(queuePlansRPC).to(plansExchange).with("key");
    }

    @Bean
    public Binding binding1(FanoutExchange fanout, Queue createPlanQueueBalancer) {return BindingBuilder.bind(createPlanQueueBalancer).to(fanout);}

    @Bean
    public  LoadBalancerReceiver receiver(){
        return new LoadBalancerReceiver();
    }

    @Bean
    public  LoadBalancerSender sender(){
        return new LoadBalancerSender();
    }
}
