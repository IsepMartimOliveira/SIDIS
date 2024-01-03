package com.example.loadbalancer.rabbitmq;

import com.example.loadbalancer.api.CreatePlanRequest;
import com.example.loadbalancer.api.DeactivatPlanRequest;
import com.example.loadbalancer.api.EditPlanRequestUpdate;
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
    @RabbitListener(queues = "#{updateQueue.name}")
    public void receiveUpdate(EditPlanRequestUpdate plans) {
        loadBalancerService.storePlanUpdate(plans);
        System.out.println(" [x] Received update message '" + plans + "' from updateQueue");
    }
    @RabbitListener(queues = "#{deactivateQueue.name}")
    public void receiveDeactivate(DeactivatPlanRequest plans) {
        loadBalancerService.storePlanDeactivates(plans);
        System.out.println(" [x] Received deactivate message '" + plans + "' from deactivateQueue");
    }

}
