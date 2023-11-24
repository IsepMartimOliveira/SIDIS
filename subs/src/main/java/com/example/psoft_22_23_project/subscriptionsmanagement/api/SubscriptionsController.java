package com.example.psoft_22_23_project.subscriptionsmanagement.api;


import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubscriptionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@Tag(name = "Subscriptions", description = "Endpoints for managing subscriptions")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionsController {

    private final SubscriptionsService service;

    private final SubscriptionsViewMapper subscriptionsViewMapper;

    private final PlansDetailsViewMapper plansDetailsViewMapper;

    @Operation(summary = "Give detailed information about a plan")
    @GetMapping
    public ResponseEntity<PlansDetailsView> planDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken) {

        final Optional<PlansDetails> plan = service.planDetails(authorizationToken);

        return ResponseEntity.ok(plansDetailsViewMapper.toPlansDetailsView(plan.get()));
    }
    @GetMapping("/external/{user}")
    public ResponseEntity<SubscriptionsView> getSubsByUserExternal(@PathVariable String user) throws IOException, URISyntaxException, InterruptedException {
        Optional<Subscriptions> subsOptional = service.findSubByUserExternal(user);
        if (subsOptional.isPresent()) {
            Subscriptions subs = subsOptional.get();
            SubscriptionsView plansView = subscriptionsViewMapper.toSubscriptionView(subs);
            return ResponseEntity.ok(plansView);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
