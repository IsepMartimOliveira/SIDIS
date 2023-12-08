package com.example.psoft_22_23_project.rabbitMQ;
import com.example.psoft_22_23_project.subscriptionsmanagement.api.*;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubscriptionsServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SubsQReceiver {
    @Autowired
    private SubscriptionsServiceImpl subsService;

    @RabbitListener(queues = "#{subsQueryQueue.name}")
    public void  createSub(CreateSubsByRabbitRequest subsRequest) {
        subsService.storeSub(subsRequest);
        System.out.println(" [x] Received '" + subsRequest + "' from subs_queue");
    }

    @RabbitListener(queues = "#{plansQueryQueueUpdate.name}")
    public void receivePlanUpdate(EditPlanRequestUpdate planRequest) {
        subsService.updatePlan(planRequest);
        System.out.println(" [x] Received '" + planRequest + "' from plans_queue");
    }
    @RabbitListener(queues = "#{plansQueryQueueDeactivate.name}")
    public void receiveDeactivate(DeactivatPlanRequest plans) {
        subsService.storePlanDeactivates(plans);
        System.out.println(" [x] Received deactivate message '" + plans + "' from deactivateQueue");
    }
    @RabbitListener(queues = "#{plansQueryQueue.name}")
    public void receivePlan(CreatePlanRequest planRequest) {
        subsService.storePlan(planRequest);
        System.out.println(" [x] Received '" + planRequest + "' from plans_queue");
    }
    @RabbitListener(queues = "#{updateQueue.name}")
    public void receiveUpdate(UpdateSubsRabbitRequest sub){
        subsService.storePlanUpdate(sub);
        System.out.println(" [x] Received update message '" + sub + "' from subsQ");
    }
    @RabbitListener(queues = "#{cancelQueue.name}")
    public void receiveCancel(UpdateSubsRabbitRequest sub){
        subsService.storeCancel(sub);
        System.out.println(" [x] Received update message '" + sub + "' from subsQ");
    }

    @RabbitListener(queues = "#{renewQueue.name}")
    public void receiveRenew(UpdateSubsRabbitRequest sub){
        subsService.storeRenew(sub);
        System.out.println(" [x] Received update message '" + sub + "' from subsQ");
    }


}
