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
import com.example.psoft_22_23_project.rabbitMQ.PlansCOMSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class PlansServiceImpl implements PlansService {
	private final PlansManager plansManager;
	private final CreatePlansMapper  createPlansMapper;
	private final PlansCOMSender plansCOMSender;
	private final  EditPlansUpdate editPlansUpdate;
	private final CreatePlansBonusMapper createPlansBonusMapper;

	@Override

	public Plans create(CreatePlanRequest resource) throws URISyntaxException, IOException, InterruptedException {
		plansManager.findByNameDoesNotExists(resource.getName());
		Plans obj = createPlansMapper.create(resource);
		plansCOMSender.send(resource);
		return obj;
	}
	public void storePlan(CreatePlanRequest resource){
		plansManager.findByNameDoesNotExists(resource.getName());
		Plans obj=createPlansMapper.create(resource);
		plansManager.save(obj);
	}
	@Override
	public Plans partialUpdate(final String name, final EditPlansRequest resource, String auth ,final long desiredVersion) throws URISyntaxException, IOException, InterruptedException {
		//ver se existe
		final Optional<Plans> plansOptional = plansManager.findByNameDoesExists(name);

		Plans plans=plansOptional.get();
		plans.updateData(desiredVersion, resource.getDescription(),
					resource.getMaximumNumberOfUsers(),
					resource.getNumberOfMinutes(),
					resource.getMusicCollection(),
					resource.getMusicSuggestion(),
					resource.getActive(),
					resource.getPromoted());

		EditPlanRequestUpdate obj=editPlansUpdate.toEditRequest(name,resource,desiredVersion);
		plansCOMSender.sendUpdate(obj);

			return plans;



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

	@Override
	public Plans deactivate(final String name, String authorizationToken,final long desiredVersion) {
		final Optional<Plans> plansOptional = plansManager.findByNameDoesExists(name);
			Plans plans=plansOptional.get();
			plans.deactivate(desiredVersion);
			DeactivatPlanRequest deactivatPlanRequest=new DeactivatPlanRequest(name,desiredVersion);
			plansCOMSender.sendDeactivate(deactivatPlanRequest);
			return plans;


	}

	@Override
	public Plans createBonus(CreatePlanRequestBonus resource) {
		plansManager.findByNameDoesNotExists(resource.getName());
		Plans obj = createPlansMapper.createBonus(resource);
		plansCOMSender.sendBonusBroadcast(resource);
		plansCOMSender.sendBonusToSubC(resource);
		return obj;

	}
	public void storeBonusPlan(CreatePlanRequestBonus resource){
		plansManager.findByNameDoesNotExists(resource.getName());
		Plans obj = createPlansMapper.createBonus(resource);
		plansManager.save(obj);
	}

	public  void  storePlanDeactivates(DeactivatPlanRequest deactivatPlanRequest){
	final Optional<Plans> plansOptional = plansManager.findByNameDoesExists(deactivatPlanRequest.getName());
	Plans plans=plansOptional.get();
	plans.deactivate(deactivatPlanRequest.getDesiredVersion());
	plansManager.save(plans);

}
	@Transactional
	public void deleteBonus(String plansBonus) {
		Optional<Plans> find=plansManager.findByNameDoesExists(plansBonus);
		try {
			plansManager.deleteByName(plansBonus);
		}catch (Exception e){
			e.printStackTrace();
		}


    }
}
