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
        idClassMapping.put("CreateSubsByRabbitRequest", com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubsByRabbitRequest.class);
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
        return new FanoutExchange("sub_to_create");
    }
    @Bean
    public FanoutExchange fanoutPlan() {return new FanoutExchange("plans_create");}

    @Bean
    public FanoutExchange fanoutPlanUpdate() {return new FanoutExchange("plans_to_update");}
    @Bean
    public FanoutExchange fanoutPlanDeactivate() {return new FanoutExchange("plans_to_deactivate");}

    @Bean
    public FanoutExchange updateFanout() {return new FanoutExchange("sub_to_update");}
    @Bean
    public FanoutExchange cancelFanout() {return new FanoutExchange("sub_to_cancel");}
    @Bean
    public FanoutExchange renewFanout() {return new FanoutExchange("sub_to_renew");}
    @Bean
    public  FanoutExchange bonus_plan(){return  new FanoutExchange("create_plan_bonus");}
    @Bean
    public  FanoutExchange updateToBonusPlan(){return  new FanoutExchange("update_to_bonus");}

    @Bean
    public  FanoutExchange createSubBonus(){return  new FanoutExchange("create_sub_bonus");}

    @Bean
    public  FanoutExchange deletePlanBonus(){return  new FanoutExchange("delete_plan");}
    @Bean
    public Queue updateQueue() {return new AnonymousQueue();}
    @Bean
    public Queue renewQueue() {return new AnonymousQueue();}
    @Bean
    public Queue cancelQueue() {return new AnonymousQueue();}

    @Bean
    public Queue plansQueryQueue() {return new AnonymousQueue();}
    @Bean
    public Queue plansQueryQueueUpdate() {return new AnonymousQueue();}

    @Bean
    public Queue plansQueryQueueDeactivate() {return new AnonymousQueue();}

    @Bean
    public Queue subsQueryQueue() {return new AnonymousQueue();}

    @Bean
    public Queue planBonusQueue(){return new AnonymousQueue();}
    @Bean
    public Queue toPlanBonusQueue(){return new AnonymousQueue();}
    @Bean
    public Queue createSubQueue(){return new AnonymousQueue();}

    @Bean
    public Queue deletePlanBonusQueue(){return new AnonymousQueue();}




    @Bean
    public Binding bindingSubsQueryQueue(FanoutExchange fanout, Queue subsQueryQueue) {
        return BindingBuilder.bind(subsQueryQueue).to(fanout);
    }
    @Bean
    public Binding bindingUpdateQueue(FanoutExchange updateFanout, Queue updateQueue) {
        return BindingBuilder.bind(updateQueue).to(updateFanout);
    }
    @Bean
    public Binding bindingCancelQueue(FanoutExchange cancelFanout, Queue cancelQueue) {
        return BindingBuilder.bind(cancelQueue).to(cancelFanout);
    }
    @Bean
    public Binding bindingRenewQueue(FanoutExchange renewFanout, Queue renewQueue) {
        return BindingBuilder.bind(renewQueue).to(renewFanout);
    }

    @Bean
    public Binding bindingPlansQueryQueue(FanoutExchange fanoutPlan, Queue plansQueryQueue) {
        return BindingBuilder.bind(plansQueryQueue).to(fanoutPlan);
    }
    @Bean
    public Binding bindingPlansQueryQueueUpdate(FanoutExchange fanoutPlanUpdate, Queue plansQueryQueueUpdate) {
        return BindingBuilder.bind(plansQueryQueueUpdate).to(fanoutPlanUpdate);
    }
    @Bean
    public Binding bindingPlansQueryQueueDeactivate(FanoutExchange fanoutPlanDeactivate, Queue plansQueryQueueDeactivate) {
        return BindingBuilder.bind(plansQueryQueueDeactivate).to(fanoutPlanDeactivate);
    }
    @Bean
    public Binding bindingBonusSaveQueue(FanoutExchange bonus_plan,Queue planBonusQueue){
        return BindingBuilder.bind(planBonusQueue).to(bonus_plan);
    }
    @Bean
    public Binding bindingToBonusQueue(FanoutExchange updateToBonusPlan,Queue toPlanBonusQueue){
        return BindingBuilder.bind(toPlanBonusQueue).to(updateToBonusPlan);
    }
    @Bean
    public Binding bindingToSubBonusQueue(FanoutExchange createSubBonus,Queue createSubQueue){
        return BindingBuilder.bind(createSubQueue).to(createSubBonus);
    }
    @Bean
    public Binding bindingToDeletePlanBonusQueue(FanoutExchange deletePlanBonus,Queue deletePlanBonusQueue){
        return BindingBuilder.bind(deletePlanBonusQueue).to(deletePlanBonus);
    }
    @Bean
    public SubsQSender sender() {
        return new SubsQSender();
    }
    @Bean
    public  SubsQReceiver receiver(){
        return new SubsQReceiver();
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
}
