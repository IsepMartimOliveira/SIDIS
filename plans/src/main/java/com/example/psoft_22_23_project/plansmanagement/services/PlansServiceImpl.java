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
import com.example.psoft_22_23_project.plansmanagement.model.FeeRevision;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.model.PromotionResult;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PlansServiceImpl implements PlansService {
	private final PlansRepository plansRepository;
	private final PlansMapperInverse plansMapperInverse;
	@Value("${server.port}")
	private int currentPort;
	@Override
	public Iterable<Plans> findAtive() throws URISyntaxException, IOException, InterruptedException {
		Iterable<Plans> planslocal = plansRepository.findByActive_Active(true);
		HttpResponse<String> plansfora = plansRepository.getPlansFromOtherAPI();
		Iterable<Plans> plans = plansRepository.addLocalPlusNot(plansfora,planslocal);
		return plans;
	}

	@Override
	public Iterable<Plans> findAtiveExternal(){
		Iterable<Plans> planslocal = plansRepository.findByActive_Active(true);
		return planslocal;
	}
	@Override
	public List<FeeRevision> history(final String name) {
		Optional<Plans> plans = plansRepository.findByName_Name(name);

		if (plans.isPresent()) {
			Plans plans1 = plans.get();
			List<FeeRevision> list = plans1.getFeeRevisions();
			return list;

		} else throw new IllegalArgumentException("Plan with name " + name + " does not exist");

	}

	public Optional<Plans> getPlanByName(String planName) throws URISyntaxException, IOException, InterruptedException {
		Optional<Plans> plans = plansRepository.findByName_Name(planName);

		if (plans.isPresent()) {
			return plans;
		}
		Optional<Plans> obj = plansRepository.getPlanByNameNotLocally(planName);
		return obj;

    }

	public Optional<Plans> getPlanByNameExternal(String planName) {
		return plansRepository.findByName_Name(planName);
	}
	@Override
	public Plans create(CreatePlanRequest resource, String auth) throws URISyntaxException, IOException, InterruptedException {
		Optional<Plans> plans = plansRepository.findByName_Name(resource.getName());
		if (plans.isPresent()) {
			throw new IllegalArgumentException("Plan with name " + resource.getName() + " already exists locally!");
		}
		Plans obj = plansRepository.createNotLocal(auth,resource);
		return plansRepository.save(obj);

	}
	@Override
	public Plans partialUpdate(final String name, final EditPlansRequest resource, String auth ,final long desiredVersion) throws URISyntaxException, IOException, InterruptedException {

		//encontrar plano localmente
		final Optional<Plans> plans = plansRepository.findByName_Name(name);
		//.orElseThrow(() -> new IllegalArgumentException("Plan with name " + name + " doesn't exists locally!"));
		if (plans.isPresent()){
			Plans plans1 = plans.get();
			plans1.updateData(desiredVersion, resource.getDescription(),
					resource.getMaximumNumberOfUsers(), resource.getNumberOfMinutes(),
					resource.getMusicCollection(), resource.getMusicSuggestion(), resource.getActive(), resource.getPromoted());
			return plansRepository.save(plans1);
		}

		Plans plan = plansRepository.updateNotLocal(resource,name,desiredVersion,auth);
		return plan;
	}

	@Override
	public Plans moneyUpdate(final String name, final EditPlanMoneyRequest resource,String auth, final long desiredVersion)throws URISyntaxException, IOException, InterruptedException {
		final Optional<Plans> plans = plansRepository.findByName_Name(name);


		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		int commaIndex = username.indexOf(",");

		String newString;
		if (commaIndex != -1) {
			newString = username.substring(commaIndex + 1); // Get the substring starting from the character after the comma
		} else {
			newString = username;
		}


		if(plans.isPresent()){
			Plans plans1 = plans.get();
			plans1.moneyData(desiredVersion, resource.getAnnualFee(), resource.getMonthlyFee(), newString);
			return plansRepository.save(plans1);

		}
		HttpResponse<String> response = plansRepository.getPlansFromOtherAPI(name,auth);

		if(response.statusCode()==200){
			Gson gson = new Gson();
			String json = gson.toJson(resource);
			HttpResponse<String> responses = plansRepository.doPlansPatchMoneyAPI(name,desiredVersion,auth,json);


			if (responses.statusCode() == 500){
				throw new IllegalArgumentException("You must issue a conditional PATCH using 'if-match' (2)!");
			}else if(responses.statusCode() == 200){

				PlanRequest planRequest = gson.fromJson(responses.body(), PlanRequest.class);

				return plansMapperInverse.toPlansView(planRequest);

				//return repository.save(plansMoney.toPlansVieMoney(editPlanMoneyRequest));
			} else if (responses.statusCode() == 409) {
				throw new IllegalArgumentException("If match number wrong!");
			}
			throw new IllegalArgumentException("Plan with name " + name + " was updated!");
		}
		else if(response.statusCode()==401){
			throw new IllegalArgumentException("Authentication failed. Please check your credentials or login to access this resource.");
		}
		throw new IllegalArgumentException("Plan with name " + name + " does not exists on another machine and locally !");

	}


	@Override
	public Plans deactivate(final String name, String authorizationToken,final long desiredVersion) throws URISyntaxException, IOException, InterruptedException {
		final Optional<Plans> plans = plansRepository.findByName_Name(name);
		if(plans.isPresent()){
			Plans plans1 = plans.get();
			if(!plans1.getActive().getActive()){
				throw new IllegalArgumentException("Plan with name " + name + " is already deactivate");
			}
			plans1.deactivate(desiredVersion);
			return plansRepository.save(plans1);
		}
		Plans obj = plansRepository.deactivateNotLocal(name,desiredVersion,authorizationToken);
		return obj;
	}

	@Override
	public PromotionResult promote(final String name, final long desiredVersion) throws IOException, InterruptedException, URISyntaxException {
		// Find the plan
		final Optional<Plans> plans = plansRepository.findByName_Name(name);
		if(plans.isPresent()){
			Plans plans1 = plans.get();
			if (!plans1.getActive().getActive()) {
				throw new IllegalArgumentException("Can't promote this plan, " + plans1.getName().getName() + " plan must be active");
			}

			// Check if plan is already promoted
			if (plans1.getPromoted().getPromoted()) {
				throw new IllegalArgumentException("Can't promote this plan, " + plans1.getName().getName() + " plan is already promoted");
			}

			PromotionResult result = new PromotionResult();
			final Optional<Plans> existingPlan = plansRepository.findByPromoted_Promoted(true);
			existingPlan.ifPresent(existingPromotedPlan -> {
				existingPromotedPlan.getPromoted().setPromoted(false);
				result.setPreviousPromotedPlan(existingPromotedPlan);
			});

			plans1.promote(desiredVersion);
			result.setNewPromotedPlan(plansRepository.save(plans1));
			existingPlan.ifPresent(plansRepository::save);
			return result;
		}
		int otherPort = (currentPort == 8081) ? 8090 : 8081;
		URI uri = new URI("http://localhost:" + otherPort + "/api/plans//byActiveAndPromoted?activePlan=true&name=" + name+"&promoted=false");

		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.GET()
				.build();

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		if (response.statusCode() == 200) {
			URI uriPromoted = new URI("http://localhost:"+otherPort+"/api/plans/byActiveAndPromoted?activePlan=true&name=" + name +"&promoted=true");
			HttpRequest requestPromoted = HttpRequest.newBuilder()
					.uri(uri)
					.GET()
					.build();

			HttpClient clientPromoted = HttpClient.newHttpClient();
			HttpResponse<String> responsePromoted = client.send(request, HttpResponse.BodyHandlers.ofString());
			Gson gson = new Gson();
			String apiUrl = "http://localhost:"+otherPort+"/api/plans/promote?name=" + name;
			HttpRequest requestpatch = HttpRequest.newBuilder()
					.uri(URI.create(apiUrl))
					.header("Content-Type", "application/json")
					.header("if-match", Long.toString(desiredVersion))
					.method("PATCH", HttpRequest.BodyPublishers.noBody()	)  // Send an empty request body
					.build();

			HttpClient httpClient = HttpClient.newHttpClient();
			HttpResponse<String> responses = httpClient.send(requestpatch, HttpResponse.BodyHandlers.ofString());

			if (responses.statusCode() == 500){
				throw new IllegalArgumentException("You must issue a conditional PATCH using 'if-match' (2)!");
			}else if(responses.statusCode() == 200){
				PlanRequestPromoted newplanRequest = gson.fromJson(responses.body(), PlanRequestPromoted.class);

				return plansMapperInverse.toPlansViewsPromote(newplanRequest);


			} else if (responses.statusCode() == 409) {
				throw new IllegalArgumentException("If match number wrong!");
			}
			throw new IllegalArgumentException("Plan with name " + name + " was updated!");
		}
		else if(response.statusCode()==401){
			throw new IllegalArgumentException("Authentication failed. Please check your credentials or login to access this resource.");
		}
		throw new IllegalArgumentException("Plan with name " + name + " does not exists on another machine and locally !");


	}


	@Override
	@Transactional
	public int cease(String name, final long desiredVersion) {
		// Retrieve the plan by name
		Plans plans = plansRepository.findByName_Name(name)
				.orElseThrow(() -> new EntityNotFoundException("Plan not found with name " + name));

		// Check if it's inactive and not promoted
		if (plans.getActive().getActive() || plans.getPromoted().getPromoted()) {
			throw new IllegalArgumentException("Can't delete this plan, it can't be active or promoted");
		}

		// Check if there are any subscriptions with non-null users for the specific plan
		//if (subscriptionsRepository.existsByPlanAndUserNotNull(plans)) {
		//	throw new IllegalArgumentException("Can't delete this plan, it has active subscriptions");
		//}

		// Perform the soft deletion if the version matches
		return plansRepository.ceaseByPlan(plans, desiredVersion);
	}

	public Optional<Plans>getPlanByActiveAndPromoted(Boolean active,String name,Boolean promoted){return plansRepository.findByActive_ActiveAndName_Name_AndPromoted_Promoted(active,name,promoted);}
	public Optional<Plans> getPlanByActive(Boolean active,String name){return  plansRepository.findByActive_ActiveAndName_Name(active,name);}



}
