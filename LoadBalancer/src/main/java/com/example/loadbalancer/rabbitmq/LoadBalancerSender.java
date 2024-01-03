package com.example.loadbalancer.rabbitmq;

import com.example.loadbalancer.api.CreatePlanRequest;
import com.example.loadbalancer.service.LoadBalancerServiceImpl;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoadBalancerSender {
    @Autowired
    private LoadBalancerServiceImpl loadBalancerService;
    @RabbitListener(queues = "rpc_plans_receiver")
    public List<CreatePlanRequest> sendPlansList() {
        return loadBalancerService.getPlansList();
    }



}
