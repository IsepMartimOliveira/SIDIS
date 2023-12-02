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
import java.util.concurrent.ExecutionException;

@Tag(name = "Subscriptions", description = "Endpoints for managing subscriptions")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionsController {

    private final SubscriptionsService service;

    private final SubscriptionsViewMapper subscriptionsViewMapper;

    private Long getVersionFromIfMatchHeader(final String ifMatchHeader) {
        if (ifMatchHeader.startsWith("\"")) {
            return Long.parseLong(ifMatchHeader.substring(1, ifMatchHeader.length() - 1));
        }
        return Long.parseLong(ifMatchHeader);
    }

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SubscriptionsView> create(@Valid @RequestBody final CreateSubscriptionsRequest resource,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) final String authorizationToken) throws URISyntaxException, IOException, InterruptedException, ExecutionException {

        final var subscriptions = service.create(resource,authorizationToken);

        final var newSubscriptionUri =
                ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(subscriptions.getPlan()).build().toUri();

        return ResponseEntity.created(newSubscriptionUri)
                .eTag(Long.toString(subscriptions.getVersion()))
                .body(subscriptionsViewMapper.toSubscriptionView(subscriptions));
    }

    @Operation(summary = "Cancel a subscription")
    @PatchMapping
    public ResponseEntity<SubscriptionsView> cancelSubscription(final WebRequest request,@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken) throws URISyntaxException, IOException, InterruptedException {
        final String ifMatchValue = request.getHeader("If-Match");
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must issue a conditional PATCH using 'if-match'");
        }

        final var subscriptions = service.cancelSubscription(authorizationToken,getVersionFromIfMatchHeader(ifMatchValue));
        return ResponseEntity.ok().eTag(Long.toString(subscriptions.getVersion())).body(subscriptionsViewMapper.toSubscriptionView(subscriptions));
    }

    @Operation(summary = "Renew annual subscription")
    @PatchMapping(value = "/renew")
    public ResponseEntity<SubscriptionsView> renewAnualSubscription(final WebRequest request,@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken) throws URISyntaxException, IOException, InterruptedException {
        final String ifMatchValue = request.getHeader("If-Match");
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must issue a conditional PATCH using 'if-match'");
        }

        final var subscriptions = service.renewAnualSubscription(authorizationToken,getVersionFromIfMatchHeader(ifMatchValue));
        return ResponseEntity.ok().eTag(Long.toString(subscriptions.getVersion())).body(subscriptionsViewMapper.toSubscriptionView(subscriptions));
    }

    @Operation(summary = "Change plan of my subscription")
    @PatchMapping(value = "/change/{name}")
    public ResponseEntity<SubscriptionsView> changePlan(final WebRequest request, @Valid @PathVariable final String name,@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken) throws URISyntaxException, IOException, InterruptedException, ExecutionException {
        final String ifMatchValue = request.getHeader("If-Match");
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must issue a conditional PATCH using 'if-match'");
        }

        final var subscriptions = service.changePlan(getVersionFromIfMatchHeader(ifMatchValue), name,authorizationToken);
        return ResponseEntity.ok().eTag(Long.toString(subscriptions.getVersion())).body(subscriptionsViewMapper.toSubscriptionView(subscriptions));
    }


}
