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
    public FanoutExchange updateFanout() {
        return new FanoutExchange("plans_to_update");
    }
    @Bean
    public FanoutExchange deactivateFanout() {
        return new FanoutExchange("plans_to_deactivate");
    }

    @Bean
    public  FanoutExchange bonus_plan(){return  new FanoutExchange("create_plan_bonus");}


    @Bean
    public  FanoutExchange deletePlanBonus(){return  new FanoutExchange("delete_plan");}
    @Bean
    public Queue plansQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue updateQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue deactivateQueue(){return new AnonymousQueue();}

    @Bean
    public Queue planBonusQueue(){return new AnonymousQueue();}

    @Bean
    public Queue deletePlanBonusQueue(){return new AnonymousQueue();}



    @Bean
    public Binding bindingUpdateQueue(FanoutExchange updateFanout, Queue updateQueue) {
        return BindingBuilder.bind(updateQueue).to(updateFanout);
    }
    @Bean
    public Binding bindingPlansQueue(FanoutExchange fanout, Queue plansQueue) {
        return BindingBuilder.bind(plansQueue).to(fanout);
    }
    @Bean
    public Binding bindingDeactivateQueue(FanoutExchange deactivateFanout,Queue deactivateQueue){
        return BindingBuilder.bind(deactivateQueue).to(deactivateFanout);
    }

    @Bean
    public Binding bindingBonusSaveQueue(FanoutExchange bonus_plan,Queue planBonusQueue){
        return BindingBuilder.bind(planBonusQueue).to(bonus_plan);
    }

    @Bean
    public Binding bindingToDeletePlanBonusQueue(FanoutExchange deletePlanBonus,Queue deletePlanBonusQueue){
        return BindingBuilder.bind(deletePlanBonusQueue).to(deletePlanBonus);
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
    public Queue sendPlanToSubBonusQueue() {return new Queue("send_plan_to_sub_bonus");}
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("plan_to_sub");
    }

    @Bean
    public Binding bindingSendPlanToSubBonusQueue(DirectExchange directExchange, Queue sendPlanToSubBonusQueue) {
        return BindingBuilder.bind(sendPlanToSubBonusQueue).to(directExchange).with("key2");
    }

}
