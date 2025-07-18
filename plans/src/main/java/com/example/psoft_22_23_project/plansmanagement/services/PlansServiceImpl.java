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
import com.example.psoft_22_23_project.plansmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.rabbitMQ.PlansQSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PlansServiceImpl implements PlansService {
	private final PlansManager plansManager;
	private  final CreatePlansMapper createPlansMapper;
	private final PlansQSender plansQSender;
	private final PlansDetailsMapper plansDetailsMapper;

	public Optional<Plans> getPlanByName(String planName){
		Optional<Plans> plans = plansManager.findByName(planName);
		return plans;
    }

	public void storePlan(CreatePlanRequest resource) {
		plansManager.findByNameDoesNotExists(resource.getName());
		Plans obj=createPlansMapper.create(resource);
		plansManager.save(obj);

	}

	@Override
	public Iterable<Plans> findAtive()  {
		Iterable<Plans> planslocal = plansManager.findByActive_Active(true);
		return planslocal;
	}

	public void storePlanUpdate(EditPlanRequestUpdate resource) {
		final Optional<Plans> plansOptional = plansManager.findByNameDoesExists(resource.getName());

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
		final Optional<Plans> plansOptional = plansManager.findByNameDoesExists(deactivatPlanRequest.getName());
		Plans plans=plansOptional.get();
		plans.deactivate(deactivatPlanRequest.getDesiredVersion());
		plansManager.save(plans);

	}

	public void getPlanDetails(String name){
		Optional<Plans> objLocal = plansManager.findByName(name);
		PlansDetails plansDetails = plansDetailsMapper.toPlansDetails(objLocal.get());
		plansQSender.sendPlanDetails(plansDetails);
	}

	public void checkPlan(String planName) {
		Optional<Plans> resultFromDB = plansManager.findByNameDoesExistsSubs(planName);
		if (resultFromDB.isPresent()){
			//plano existe
			Boolean b = true;
			plansQSender.sendPlanCheck(b);
		}else {
			//plano n existe
			Boolean b = false;
			plansQSender.sendPlanCheck(b);
		}

	}

	public void storeBonusPlan(CreatePlanRequestBonus resource){
		plansManager.findByNameDoesNotExists(resource.getName());
		Plans obj = createPlansMapper.createBonus(resource);
		plansManager.save(obj);
	}

    public void deleteBonus(String plansBonus) {
    }
}
