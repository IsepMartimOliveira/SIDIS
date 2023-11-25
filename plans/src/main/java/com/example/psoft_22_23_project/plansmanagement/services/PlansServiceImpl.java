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
package com.example.psoft_22_23_project.plansmanagement.services;

import com.example.psoft_22_23_project.plansmanagement.api.*;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PlansServiceImpl implements PlansService {
	private final PlansManagerImpl plansManager;
	private final PlansMapperInverse plansMapperInverse;
	private  final CreatePlansMapper createPlansMapper;

	@Override
	public Iterable<Plans> findAtiveExternal(){
		Iterable<Plans> planslocal = plansManager.findByActive_Active(true);
		return planslocal;
	}

	public Optional<Plans> getPlanByName(String planName) throws URISyntaxException, IOException, InterruptedException {
		Optional<Plans> plans = plansManager.findByName(planName);
		return plans;
    }

	public Optional<Plans> getPlanByNameExternal(String planName) throws IOException, URISyntaxException, InterruptedException {
		return plansManager.findByNameName(planName);
	}


	public void storePlan(CreatePlanRequest resource) {
		plansManager.findByNameDoesNotExists(resource.getName());
		Plans obj=createPlansMapper.create(resource);
		plansManager.save(obj);

	}

	@Override
	public Iterable<Plans> findAtive() throws URISyntaxException, IOException, InterruptedException {
		Iterable<Plans> planslocal = plansManager.findByActive_Active(true);
		PlanRequest plans = plansManager.getAllExternal();
		Plans newPlan = plansMapperInverse.toPlansView(plans);
		Iterable<Plans> getAll=plansManager.addPlanToIterable(planslocal,newPlan);
		return getAll;
	}

	public void storePlanUpdate(EditPlanRequestUpdate resource) {
		final Optional<Plans> plansOptional = plansManager.findByNameDoesExistsUpdate(resource.getName());

		Plans plans=plansOptional.get();
		plans.updateData(resource.getDesiredVersion(), resource.getEditPlansRequest().getDescription(),
				resource.getEditPlansRequest().getMaximumNumberOfUsers(),
				resource.getEditPlansRequest().getNumberOfMinutes(),
				resource.getEditPlansRequest().getMusicCollection(),
				resource.getEditPlansRequest().getMusicSuggestion(),
				resource.getEditPlansRequest().getActive(),
				resource.getEditPlansRequest().getPromoted());

		plansManager.save(plans);
	}
	public  void  storePlanDeactivates(DeactivatPlanRequest deactivatPlanRequest){
		final Optional<Plans> plansOptional = plansManager.findByNameDoesExistsUpdate(deactivatPlanRequest.getName());
		Plans plans=plansOptional.get();
		plans.deactivate(deactivatPlanRequest.getDesiredVersion());
		plansManager.save(plans);

	}
}
