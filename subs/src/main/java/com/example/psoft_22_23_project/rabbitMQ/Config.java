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
    public FanoutExchange getPlanNameFanout() {
        return new FanoutExchange("get_plan");
    }
    @Bean
    public FanoutExchange sendPlanDetailsFanout() {
        return new FanoutExchange("send_plan_detail");
    }

    @Bean
    public Queue sendPlanDetailsQueue() {
        return new AnonymousQueue();
    }
    @Bean
    public Queue getPlanNameQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue subsQueryQueue() {
        return new AnonymousQueue();
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
    public Binding bindingPlansQueryQueue(FanoutExchange fanout, Queue subsQueryQueue) {
        return BindingBuilder.bind(subsQueryQueue).to(fanout);
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