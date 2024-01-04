package com.example.psoft_22_23_project.rabbitMQ;

import org.springframework.amqp.core.*;
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
public class Config {

    @Bean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("CreatePlanRequest", com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest.class);
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
    public FanoutExchange getPlanNameFanout() {
        return new FanoutExchange("get_plan");
    }
    @Bean
    public FanoutExchange sendPlanDetailsFanout() {
        return new FanoutExchange("send_plan_detail");
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
    public FanoutExchange checkPlanFanout() {
        return new FanoutExchange("checkPlan");
    }
    @Bean
    public FanoutExchange checkSendPlanFanout() {
        return new FanoutExchange("send_check_plan");
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
    public Queue plansQueryQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Queue updateQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue deactivateQueue(){return new AnonymousQueue();}


    @Bean
    public Queue sendPlanDetailsQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Queue getPlanNameQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Queue checkPlanQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Queue checkSendPlanQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue planBonusQueue(){return new AnonymousQueue();}
    @Bean
    public Queue deletePlanBonusQueue(){return new AnonymousQueue();}

    @Bean
    public Binding bindingCheckPlanQueue(FanoutExchange checkPlanFanout, Queue checkPlanQueue) {
        return BindingBuilder.bind(checkPlanQueue).to(checkPlanFanout);
    }
    @Bean
    public Binding bindingSendCheckPlanQueue(FanoutExchange checkSendPlanFanout, Queue checkSendPlanQueue) {
        return BindingBuilder.bind(checkSendPlanQueue).to(checkSendPlanFanout);
    }
    @Bean
    public Binding bindingGetPlanNameQueue(FanoutExchange getPlanNameFanout, Queue getPlanNameQueue) {
        return BindingBuilder.bind(getPlanNameQueue).to(getPlanNameFanout);
    }
    @Bean
    public Binding bindingSendPlanDetailsQueue(FanoutExchange sendPlanDetailsFanout, Queue sendPlanDetailsQueue) {
        return BindingBuilder.bind(sendPlanDetailsQueue).to(sendPlanDetailsFanout);
    }

    @Bean
    public Binding bindingPlansQueryQueue(FanoutExchange fanout, Queue plansQueryQueue) {
        return BindingBuilder.bind(plansQueryQueue).to(fanout);
    }
    @Bean
    public Binding bindingUpdateQueue(FanoutExchange updateFanout, Queue updateQueue) {
        return BindingBuilder.bind(updateQueue).to(updateFanout);
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
    public  PlansQReceiver receiver(){
        return new PlansQReceiver();
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

}
