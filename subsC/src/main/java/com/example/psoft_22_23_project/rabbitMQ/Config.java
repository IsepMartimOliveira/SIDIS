package com.example.psoft_22_23_project.rabbitMQ;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.plaf.PanelUI;

@Configuration
public class Config {

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange("sub_to_create");
    }
    @Bean
    public FanoutExchange deleteFanout() {
        return new FanoutExchange("sub_to_delete");
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
    public FanoutExchange checkSendPlanFanout() {
        return new FanoutExchange("send_check_plan");
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
    public FanoutExchange receivePlanBonus(){return new FanoutExchange("create_plan_bonus");}

    @Bean
    public  FanoutExchange updateToBonusPlan(){return  new FanoutExchange("update_to_bonus");}

    @Bean
    public Queue sendPlanDetailsQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Queue getPlanNameQueue() {
        return new AnonymousQueue();
    }


    @Bean
    public Queue subQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Queue subsQ2() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue subsC2() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue subsQ() {
        return new AnonymousQueue();
    }
    @Bean
    public Queue updateQueue() {
        return new AnonymousQueue();
    }
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
    public Queue planBonusQueue(){return new AnonymousQueue();}

    @Bean
    public Queue toPlanBonusQueue(){return new AnonymousQueue();}
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
    public Binding bindingBonusQue(FanoutExchange receivePlanBonus,Queue planBonusQueue){
        return BindingBuilder.bind(planBonusQueue).to(receivePlanBonus);
    }

    @Bean
    public Binding bindingToBonusQueue(FanoutExchange updateToBonusPlan,Queue toPlanBonusQueue){
        return BindingBuilder.bind(toPlanBonusQueue).to(updateToBonusPlan);
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
