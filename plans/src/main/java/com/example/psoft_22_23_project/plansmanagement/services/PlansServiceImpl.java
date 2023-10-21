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
import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PlansServiceImpl implements PlansService {
	private final PlansRepository repository;
	//private final SubscriptionsRepository subscriptionsRepository;

	private final CreatePlansMapper createPlansMapper;
	private final PlansMapperInverse plansMapperInverse;
	@Value("${server.port}")
	private int currentPort;


	@Override
	public Iterable<Plans> findAtive() throws URISyntaxException, IOException, InterruptedException {
		Iterable<Plans> planslocal = repository.findByActive_Active(true);

		HttpResponse<String> plansfora = repository.getPlansFromOtherAPI();

		JSONArray jsonArray = new JSONArray(plansfora.body());

		for (int i = 0; i < jsonArray.length(); i++) {
			if (repository.findByName_Name(jsonArray.getJSONObject(i).getString("name")).isEmpty()){
				PlanRequest newPlan = new PlanRequest(
						jsonArray.getJSONObject(i).getString("name"),
						jsonArray.getJSONObject(i).getString("description"),
						jsonArray.getJSONObject(i).getString("numberOfMinutes"),
						jsonArray.getJSONObject(i).getString("maximumNumberOfUsers"),
						jsonArray.getJSONObject(i).getString("musicCollection"),
						jsonArray.getJSONObject(i).getString("musicSuggestion"),
						jsonArray.getJSONObject(i).getString("annualFee"),
						jsonArray.getJSONObject(i).getString("monthlyFee"),
						jsonArray.getJSONObject(i).getString("active"),
						jsonArray.getJSONObject(i).getString("promoted")
				);
				Plans obj = plansMapperInverse.toPlansView(newPlan);
				planslocal = addPlanToIterable(planslocal, obj);
			}
		}
		return planslocal;
	}

	private Iterable<Plans> addPlanToIterable(Iterable<Plans> plans, Plans newPlan) {
		if (plans instanceof List) {
			List<Plans> planList = new ArrayList<>((List<Plans>) plans);
			planList.add(newPlan);
			return planList;
		}
		// Handle other types of Iterables if necessary
		return plans;
	}
	@Override
	public List<FeeRevision> history(final String name) {
		Optional<Plans> plans = repository.findByName_Name(name);

		if (plans.isPresent()) {
			Plans plans1 = plans.get();
			List<FeeRevision> list = plans1.getFeeRevisions();
			return list;

		} else throw new IllegalArgumentException("Plan with name " + name + " does not exist");

	}

	public Optional<Plans> getPlanByName(String planName) throws URISyntaxException, IOException, InterruptedException {

		Optional<Plans> plans = repository.findByName_Name(planName);

		if (plans.isPresent()) {
			return plans;
		}
		HttpResponse<String> plan = repository.findPlanFromOtherAPI(planName);
		if (plan.statusCode() == 200) {
			JSONObject jsonArray = new JSONObject(plan.body());

			PlanRequest newPlan = new PlanRequest(
					jsonArray.getString("name"),
					jsonArray.getString("description"),
					jsonArray.getString("numberOfMinutes"),
					jsonArray.getString("maximumNumberOfUsers"),
					jsonArray.getString("musicCollection"),
					jsonArray.getString("musicSuggestion"),
					jsonArray.getString("annualFee"),
					jsonArray.getString("monthlyFee"),
					jsonArray.getString("active"),
					jsonArray.getString("promoted")
			);
			Plans obj = plansMapperInverse.toPlansView(newPlan);
			return Optional.ofNullable(obj);
		}else {
			throw new IllegalArgumentException("Plan with name " + planName + " already exists locally!");
		}

    }
	@Override
	public Plans create(CreatePlanRequest resource) throws URISyntaxException, IOException, InterruptedException {
		Optional<Plans> plans = repository.findByName_Name(resource.getName());
		if (plans.isPresent()) {
			throw new IllegalArgumentException("Plan with name " + resource.getName() + " already exists locally!");
		}
		HttpResponse<String> response = repository.getPlansFromOtherAPI(resource.getName());
		if(response.statusCode() == 404){
			Plans obj = createPlansMapper.create(resource);
			return repository.save(obj);

		}else {
			throw new IllegalArgumentException("Plan with name " + resource.getName() + " already exists on another machine!");
		}

	}




	@Override
	public Plans partialUpdate(final String name, final EditPlansRequest resource, final long desiredVersion) throws URISyntaxException, IOException, InterruptedException {

		//encontrar plano localmente
		final Optional<Plans> plans = repository.findByName_Name(name);
				//.orElseThrow(() -> new IllegalArgumentException("Plan with name " + name + " doesn't exists locally!"));
		if (plans.isPresent()){
			Plans plans1 = plans.get();
			plans1.updateData(desiredVersion, resource.getDescription(),
					resource.getMaximumNumberOfUsers(), resource.getNumberOfMinutes(),
					resource.getMusicCollection(), resource.getMusicSuggestion(), resource.getActive(), resource.getPromoted());
			return repository.save(plans1);
		}

		HttpResponse<String> response = repository.getPlansFromOtherAPI(name);

		if (response.statusCode() == 200) {

			Gson gson = new Gson();
			String json = gson.toJson(resource);
			HttpResponse<String> responses = repository.doPlansPatchAPI(name,desiredVersion,json);


			if (responses.statusCode() == 500){
				throw new IllegalArgumentException("You must issue a conditional PATCH using 'if-match' (2)!");
			}else if(responses.statusCode() == 200){
				//
				PlanRequest planRequest = gson.fromJson(responses.body(), PlanRequest.class);
				//
				return plansMapperInverse.toPlansView(planRequest);
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
	public Plans moneyUpdate(final String name, final EditPlanMoneyRequest resource, final long desiredVersion)throws URISyntaxException, IOException, InterruptedException {
		final Optional<Plans> plans = repository.findByName_Name(name);


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
			return repository.save(plans1);

		}
		HttpResponse<String> response = repository.getPlansFromOtherAPI(name);

		if(response.statusCode()==200){
			Gson gson = new Gson();
			String json = gson.toJson(resource);
			HttpResponse<String> responses = repository.doPlansPatchMoneyAPI(name,desiredVersion,json);


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
	public Plans deactivate(final String name, final long desiredVersion) throws URISyntaxException, IOException, InterruptedException {
		final Optional<Plans> plans = repository.findByName_Name(name);
		if(plans.isPresent()){
			Plans plans1 = plans.get();
			if(!plans1.getActive().getActive()){
				throw new IllegalArgumentException("Plan with name " + name + " is already deactivate");
			}
			plans1.deactivate(desiredVersion);
			return repository.save(plans1);

		}

		HttpResponse<String> response = repository.getPlansFromOtherAPI(name);


		if (response.statusCode() == 200) {
			Gson gson = new Gson();
			HttpResponse<String> responses=repository.doPlansPatchDeactivate(name,desiredVersion);


			if (responses.statusCode() == 500){
				throw new IllegalArgumentException("You must issue a conditional PATCH using 'if-match' (2)!");
			}else if(responses.statusCode() == 200){

				PlanRequest planRequest = gson.fromJson(responses.body(), PlanRequest.class);
				return plansMapperInverse.toPlansView(planRequest);

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
	public PromotionResult promote(final String name, final long desiredVersion) throws IOException, InterruptedException, URISyntaxException {
		// Find the plan
		final Optional<Plans> plans = repository.findByName_Name(name);
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
			final Optional<Plans> existingPlan = repository.findByPromoted_Promoted(true);
			existingPlan.ifPresent(existingPromotedPlan -> {
				existingPromotedPlan.getPromoted().setPromoted(false);
				result.setPreviousPromotedPlan(existingPromotedPlan);
			});

			plans1.promote(desiredVersion);
			result.setNewPromotedPlan(repository.save(plans1));
			existingPlan.ifPresent(repository::save);
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
		Plans plans = repository.findByName_Name(name)
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
		return repository.ceaseByPlan(plans, desiredVersion);
	}

	public Optional<Plans>getPlanByActiveAndPromoted(Boolean active,String name,Boolean promoted){return repository.findByActive_ActiveAndName_Name_AndPromoted_Promoted(active,name,promoted);}
	public Optional<Plans> getPlanByActive(Boolean active,String name){return  repository.findByActive_ActiveAndName_Name(active,name);}



}
