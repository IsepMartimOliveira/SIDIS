package com.example.loadbalancer.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRabbit
public class Config {

    @Bean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("CreatePlanRequest", com.example.loadbalancer.api.CreatePlanRequest.class);
        idClassMapping.put("CreateSubsByRabbitRequest", com.example.loadbalancer.api.CreateSubsByRabbitRequest.class);
        typeMapper.setIdClassMapping(idClassMapping);
        converter.setJavaTypeMapper(typeMapper);
        return converter;
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
    public FanoutExchange updateFanout() {
        return new FanoutExchange("plans_to_update");
    }
    @Bean
    public  FanoutExchange bonus_plan(){return  new FanoutExchange("create_plan_bonus");}
    @Bean
    public Queue planBonusQueue(){return new AnonymousQueue();}
    @Bean
    public Binding bindingBonusSaveQueue(FanoutExchange bonus_plan,Queue planBonusQueue){
        return BindingBuilder.bind(planBonusQueue).to(bonus_plan);
    }
    @Bean
    public Queue createPlanQueueBalancer(){return new AnonymousQueue();}
    @Bean
    public Queue updateQueue() {
        return new AnonymousQueue();
    }


    @Bean
    public Queue deactivateQueue(){return new AnonymousQueue();}
    @Bean
    public FanoutExchange deactivateFanout() {
        return new FanoutExchange("plans_to_deactivate");
    }

    @Bean
    public Binding bindingDeactivateQueue(FanoutExchange deactivateFanout,Queue deactivateQueue){
        return BindingBuilder.bind(deactivateQueue).to(deactivateFanout);
    }
    @Bean
    public  FanoutExchange deletePlanBonus(){return  new FanoutExchange("delete_plan");}
    @Bean
    public Queue deletePlanBonusQueue(){return new AnonymousQueue();}

    @Bean
    public FanoutExchange fanoutSub() {
        return new FanoutExchange("sub_to_create");
    }
    @Bean
    public Queue subQueue() {return new AnonymousQueue();}

    @Bean
    public Binding bindingSubQueue(FanoutExchange fanoutSub, Queue subQueue) {
        return BindingBuilder.bind(subQueue).to(fanoutSub);
    }

    @Bean
    public Binding bindingToDeletePlanBonusQueue(FanoutExchange deletePlanBonus,Queue deletePlanBonusQueue){
        return BindingBuilder.bind(deletePlanBonusQueue).to(deletePlanBonus);
    }
    @Bean
    public Binding binding1(FanoutExchange fanout, Queue createPlanQueueBalancer) {return BindingBuilder.bind(createPlanQueueBalancer).to(fanout);}
    @Bean
    public Binding bindingUpdateQueue(FanoutExchange updateFanout, Queue updateQueue) {
        return BindingBuilder.bind(updateQueue).to(updateFanout);
    }
    @Bean
    public  LoadBalancerReceiver receiver(){
        return new LoadBalancerReceiver();
    }

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
    public Queue queueSubsRPC(){
        return new Queue("rpc_subs_receiver");
    }
    @Bean
    public DirectExchange subsExchange(){
        return new DirectExchange("rpc_subs");
    }
    @Bean
    public Binding bindingReceiverSubs(DirectExchange subsExchange, Queue queueSubsRPC){
        return BindingBuilder.bind(queueSubsRPC).to(subsExchange).with("key");
    }

    @Bean
    public  LoadBalancerSender sender(){
        return new LoadBalancerSender();
    }
}
