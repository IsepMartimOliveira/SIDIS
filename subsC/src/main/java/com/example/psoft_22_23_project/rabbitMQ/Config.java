package com.example.psoft_22_23_project.rabbitMQ;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.plaf.PanelUI;
import java.util.UUID;
import java.util.logging.Logger;

@Configuration
public class Config {
 private final UUID  uid= UUID.randomUUID();
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange("sub_to_create");
    }
    @Bean
    public FanoutExchange updateFanout() {
        return new FanoutExchange("sub_to_update");
    }
    @Bean
    public FanoutExchange cancelFanout() {
        return new FanoutExchange("sub_to_cancel");
    }
    @Bean
    public FanoutExchange renewFanout() {
        return new FanoutExchange("sub_to_renew");
    }
    @Bean
    public FanoutExchange checkPlanFanout() {
        return new FanoutExchange("checkPlan");
    }
    @Bean
    public FanoutExchange checkSendPlanFanout() {return new FanoutExchange("send_check_plan");}
    @Bean
    public FanoutExchange getPlanNameFanout() {
        return new FanoutExchange("get_plan");
    }
    @Bean
    public FanoutExchange sendPlanDetailsFanout() {
        return new FanoutExchange("send_plan_detail");
    }
    @Bean
    public  FanoutExchange updateToBonusPlan(){return  new FanoutExchange("update_to_bonus");}
    @Bean
    public  FanoutExchange createSubBonus(){return  new FanoutExchange("create_sub_bonus");}
    @Bean
    public  FanoutExchange deletePlanBonus(){return  new FanoutExchange("delete_plan");}
    @Bean
    public Queue sendPlanDetailsQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Queue getPlanNameQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Queue subQueue() {return new AnonymousQueue();}
    @Bean
    public Queue updateQueue() {return new AnonymousQueue();}
    @Bean
    public Queue renewQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Queue cancelQueue() {
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
    public Queue sendPlanToSubBonusQueue() {return new Queue("send_plan_to_sub_bonus");}
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("plan_to_sub");
    }

    @Bean
    public Binding bindingSendPlanToSubBonusQueue(DirectExchange directExchange, Queue sendPlanToSubBonusQueue) {
        return BindingBuilder.bind(sendPlanToSubBonusQueue).to(directExchange).with("key2");
    }

    @Bean
    public Queue toPlanBonusQueue(){return new AnonymousQueue();}

    @Bean
    public Queue createSubQueue(){return new AnonymousQueue();}

    @Bean
    public Queue deletePlanBonusQueue(){
        Logger.getGlobal().info(uid.toString());
        return new Queue("delete");}


    @Bean
    public Binding bindingSendCheckPlanQueue(FanoutExchange checkSendPlanFanout, Queue checkSendPlanQueue) {
        return BindingBuilder.bind(checkSendPlanQueue).to(checkSendPlanFanout);
    }

    @Bean
    public Binding bindingCheckPlanQueue(FanoutExchange checkPlanFanout, Queue checkPlanQueue) {
        return BindingBuilder.bind(checkPlanQueue).to(checkPlanFanout);
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
    public Binding bindingSubQueue(FanoutExchange fanout, Queue subQueue) {
        return BindingBuilder.bind(subQueue).to(fanout);
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
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public SubsCOMSender sender() {
        return new SubsCOMSender();
    }
    @Bean
    public SubsCOMReceiver receiver(){
        return new SubsCOMReceiver();
    }


}
