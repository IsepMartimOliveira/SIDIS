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
    public  FanoutExchange bonus_plan(){return  new FanoutExchange("create_plan_bonus");}



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
    public SubsQSender sender() {
        return new SubsQSender();
    }
    @Bean
    public  SubsQReceiver receiver(){
        return new SubsQReceiver();
    }
}
