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

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import com.example.psoft_22_23_project.plansmanagement.api.EditPlansRequest;
import com.example.psoft_22_23_project.plansmanagement.api.PlanRequest;
import com.example.psoft_22_23_project.plansmanagement.api.PlansMapperInverse;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.rabbitMQ.PlansCOMSender;
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
	private final CreatePlansMapper  createPlansMapper;
	private final PlansCOMSender plansCOMSender;
	@Override
	public Iterable<Plans> findAtive() throws URISyntaxException, IOException, InterruptedException {
		Iterable<Plans> planslocal = plansManager.findByActive_Active(true);
		PlanRequest plans = plansManager.getAllExternal();
		Plans newPlan = plansMapperInverse.toPlansView(plans);
		Iterable<Plans> getAll=plansManager.addPlanToIterable(planslocal,newPlan);
		return getAll;
	}

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
	@Override
	public Plans create(CreatePlanRequest resource, String auth) throws URISyntaxException, IOException, InterruptedException {
		plansManager.findByNameDoesNotExists(resource.getName());
		plansCOMSender.send(resource);
		Plans obj = createPlansMapper.create(resource);
		return plansManager.save(obj);
	}
	@Override
	public Plans partialUpdate(final String name, final EditPlansRequest resource, String auth ,final long desiredVersion) throws URISyntaxException, IOException, InterruptedException {
		//ver se existe
		final Optional<Plans> plansOptional = plansManager.findByNameDoesExistsUpdate(name,desiredVersion,resource,auth);

		if(plansOptional.isPresent()){
			Plans plans=plansOptional.get();
			plans.updateData(desiredVersion, resource.getDescription(),
					resource.getMaximumNumberOfUsers(),
					resource.getNumberOfMinutes(),
					resource.getMusicCollection(),
					resource.getMusicSuggestion(),
					resource.getActive(),
					resource.getPromoted());
			return plansManager.save(plans);


		}
		throw new IllegalArgumentException("Plan with name " + name + " doesn´t exist exists!");
	}
	@Override
	public Plans deactivate(final String name, String authorizationToken,final long desiredVersion) throws URISyntaxException, IOException, InterruptedException {
		final Optional<Plans> plansOptional = plansManager.findByNameDoesExists(name,desiredVersion,authorizationToken);
		if(plansOptional.isPresent()){
			Plans plans=plansOptional.get();
			plans.deactivate(desiredVersion);
			return plansManager.save(plans);
		}
		throw new IllegalArgumentException("Plan with name " + name + " doesn´t exist exists!");
	}

}
