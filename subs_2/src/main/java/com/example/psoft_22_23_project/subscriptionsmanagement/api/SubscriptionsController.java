package com.example.psoft_22_23_project.subscriptionsmanagement.api;


import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubscriptionsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Subscriptions", description = "Endpoints for managing subscriptions")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionsController {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionsController.class);

    private final SubscriptionsService service;


    private final SubscriptionsViewMapper subscriptionsViewMapper;

    private final PlansDetailsViewMapper plansDetailsViewMapper;
/*
    private Long getVersionFromIfMatchHeader(final String ifMatchHeader) {
        if (ifMatchHeader.startsWith("\"")) {
            return Long.parseLong(ifMatchHeader.substring(1, ifMatchHeader.length() - 1));
        }
        return Long.parseLong(ifMatchHeader);
    }

    @Operation(summary = "Gets all subscriptions")
    @GetMapping(value = "/list")
    public Iterable<SubscriptionsView> findAll() {
        return subscriptionsViewMapper.toSubscriptionsView(service.findAll());
    }


    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SubscriptionsView> create(@Valid @RequestBody final CreateSubscriptionsRequest resource) {

        final var subscriptions = service.create(resource);

        final var newSubscriptionUri =
                ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(subscriptions.getPlan().getName().getName()).build().toUri();

        return ResponseEntity.created(newSubscriptionUri)
                .eTag(Long.toString(subscriptions.getVersion()))
                .body(subscriptionsViewMapper.toSubscriptionView(subscriptions));
    }

    @Operation(summary = "Cancel a subscription")
    @PatchMapping
    public ResponseEntity<SubscriptionsView> cancelSubscription(final WebRequest request) {
        final String ifMatchValue = request.getHeader("If-Match");
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must issue a conditional PATCH using 'if-match'");
        }

        final var subscriptions = service.cancelSubscription(getVersionFromIfMatchHeader(ifMatchValue));
        return ResponseEntity.ok().eTag(Long.toString(subscriptions.getVersion())).body(subscriptionsViewMapper.toSubscriptionView(subscriptions));
    }


    @Operation(summary = "Give detailed information about a plan")
    @GetMapping
    public ResponseEntity<PlansDetailsView> planDetails() {

        final PlansDetails plan = service.planDetails();

        return ResponseEntity.ok(plansDetailsViewMapper.toPlansDetailsView(plan));
    }

    @Operation(summary = "Renew annual subscription")
    @PatchMapping(value = "/renew")
    public ResponseEntity<SubscriptionsView> renewAnualSubscription(final WebRequest request) {
        final String ifMatchValue = request.getHeader("If-Match");
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must issue a conditional PATCH using 'if-match'");
        }

        final var subscriptions = service.renewAnualSubscription(getVersionFromIfMatchHeader(ifMatchValue));
        return ResponseEntity.ok().eTag(Long.toString(subscriptions.getVersion())).body(subscriptionsViewMapper.toSubscriptionView(subscriptions));
    }


    @Operation(summary = "Change plan of my subscription")
    @PatchMapping(value = "/change/{name}")
    public ResponseEntity<SubscriptionsView> changePlan(final WebRequest request, @Valid @PathVariable final String name) {
        final String ifMatchValue = request.getHeader("If-Match");
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must issue a conditional PATCH using 'if-match'");
        }

        final var subscriptions = service.changePlan(getVersionFromIfMatchHeader(ifMatchValue), name);
        return ResponseEntity.ok().eTag(Long.toString(subscriptions.getVersion())).body(subscriptionsViewMapper.toSubscriptionView(subscriptions));
    }

    @Operation(summary = "Change plan of my subscription")
    @PatchMapping(value = "/change/{actualPlan}/{newPlan}")
    public void migrateAllToPlan(final WebRequest request,@Valid @PathVariable final String actualPlan, @Valid @PathVariable final String newPlan) {
        final String ifMatchValue = request.getHeader("If-Match");
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must issue a conditional PATCH using 'if-match'");
        }

        service.migrateAllToPlan(getVersionFromIfMatchHeader(ifMatchValue), actualPlan, newPlan);
    }

 */

}
