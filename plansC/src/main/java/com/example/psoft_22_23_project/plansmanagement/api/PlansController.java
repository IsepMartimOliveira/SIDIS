/*
 * Copyright (c) 2022-2022 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.example.psoft_22_23_project.plansmanagement.api;

import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.services.PlansService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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


@Tag(name = "Plans", description = "Endpoints for managing plans")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans")
public class PlansController {

	private final PlansService service;

	private final PlansViewMapper plansViewMapper;


	private Long getVersionFromIfMatchHeader(final String ifMatchHeader) {
		if (ifMatchHeader.startsWith("\"")) {
			return Long.parseLong(ifMatchHeader.substring(1, ifMatchHeader.length() - 1));
		}
		return Long.parseLong(ifMatchHeader);
	}

	@Operation(summary = "Create a plan Bonus")
	@PostMapping("/createBonus")
	@ResponseStatus(HttpStatus.ACCEPTED)public ResponseEntity<PlansView>createBonus(@Valid@RequestBody final CreatePlanRequestBonus resource){
		final Plans planBonus=service.createBonus(resource);
		final var newPlanUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
				.pathSegment(planBonus.getName().getName()).build().toUri();

		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.location(newPlanUri)
				.eTag(Long.toString(planBonus.getVersion()))
				.body(plansViewMapper.toPlansView(planBonus));
	}

	@Operation(summary = "Creates a new Plan")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED) public ResponseEntity<PlansView>
	create(@Valid @RequestBody final CreatePlanRequest resource) throws URISyntaxException, IOException, InterruptedException {
		final Plans plan = service.create(resource);
		final var newPlanUri =
				ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(plan.getName().getName()).build() .toUri();
		return
				ResponseEntity.created(newPlanUri).eTag(Long.toString(plan.getVersion()))
						.body(plansViewMapper.toPlansView(plan));
	}

	@Operation(summary = "Partially updates an existing plan")
	@PatchMapping(value = "/update/{name}")
	public ResponseEntity<PlansView> partialUpdate(final WebRequest request,
												 @PathVariable("name") @Parameter(description = "The name of the plan to update") final String name,
												   @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
												 @Valid @RequestBody final EditPlansRequest resource) throws URISyntaxException, IOException, InterruptedException {

		final String ifMatchValue = request.getHeader("If-Match");
		if (ifMatchValue == null || ifMatchValue.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"You must issue a conditional PATCH using 'if-match'");
		}

		final var plans = service.partialUpdate(name, resource, authorizationToken,getVersionFromIfMatchHeader(ifMatchValue));
		return ResponseEntity.ok().eTag(Long.toString(plans.getVersion())).body(plansViewMapper.toPlansView(plans));
	}

	@Operation(summary = "Deactivate a plan")
	@PatchMapping(value = "/deactivate/{name}")
	public ResponseEntity<PlansView> deactivate(final WebRequest request,
												@PathVariable("name") @Parameter(description = "The name of the plan to update") final String name,
												@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken) throws URISyntaxException, IOException, InterruptedException {
		final String ifMatchValue = request.getHeader("If-Match");
		if (ifMatchValue == null || ifMatchValue.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"You must issue a conditional PATCH using 'if-match'");
		}

		final var plans = service.deactivate(name, authorizationToken,getVersionFromIfMatchHeader(ifMatchValue));
		return ResponseEntity.ok().eTag(Long.toString(plans.getVersion())).body(plansViewMapper.toPlansView(plans));
	}

}


