package com.example.loadbalancer.rabbitmq;

import com.example.loadbalancer.api.CreatePlanRequest;
import com.example.loadbalancer.services.LoadBalancerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class LoadBalancerSender {

    @Autowired
    private LoadBalancerService loadBalancerService;

    @RabbitListener(queues = "rpc_plans_receiver")
    public List<CreatePlanRequest> sendPlans() {
        return loadBalancerService.getPlansList();
    }

}
