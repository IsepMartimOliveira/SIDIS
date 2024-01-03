package com.example.loadbalancer.rabbitmq;

import com.example.loadbalancer.api.CreatePlanRequest;
import com.example.loadbalancer.service.LoadBalancerServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoadBalancerReceiver {
    @Autowired
    private LoadBalancerServiceImpl loadBalancerService;
    @RabbitListener(queues = "#{createPlanQueueBalancer.name}")
    public void receivePlan(CreatePlanRequest planRequest) {
        loadBalancerService.createPlan(planRequest);
        System.out.println(" [x] Received '" + planRequest + "' from loadBalancer");
    }
}
