package com.example.psoft_22_23_project.subscriptionsmanagement.api;


import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubscriptionsService2;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Tag(name = "Subscriptions2", description = "Endpoints for managing subscriptions")
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions2")
public class SubscriptionsController2 {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionsController2.class);

    private final SubscriptionsService2 service;
    private final SubscriptionsViewMapper subscriptionsViewMapper;

    private Long getVersionFromIfMatchHeader(final String ifMatchHeader) {
        if (ifMatchHeader.startsWith("\"")) {
            return Long.parseLong(ifMatchHeader.substring(1, ifMatchHeader.length() - 1));
        }
        return Long.parseLong(ifMatchHeader);
    }

    @GetMapping("/{user}")
    public ResponseEntity<SubscriptionsView> getSubsByPlanAndUser(@PathVariable String user){
        user = user + "@mail.com";
        Optional<Subscriptions> subsOptional = service.findSubByUserAndPlan(user);
        if (subsOptional.isPresent()) {
            Subscriptions subs = subsOptional.get();
            SubscriptionsView plansView = subscriptionsViewMapper.toSubscriptionView(subs);
            return ResponseEntity.ok(plansView);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
