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
package com.example.psoft_22_23_project.plansmanagement.repositories;

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import com.example.psoft_22_23_project.plansmanagement.api.EditPlansRequest;
import com.example.psoft_22_23_project.plansmanagement.api.PlanRequest;
import com.example.psoft_22_23_project.plansmanagement.api.PlansMapperInverse;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.services.CreatePlansMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Configuration
public interface PlansRepository extends PlansRepositoryDB,PlansRepoHttpCustom{

}

interface PlansRepositoryDB extends CrudRepository<Plans,Long> {
	Optional<Plans> findByName_Name(@NotNull String name);

	Optional<Plans> findByActive_ActiveAndName_Name_AndPromoted_Promoted(@NotNull boolean active, @NotNull String name_name, @NotNull boolean promoted);

	Optional<Plans> findByActive_ActiveAndName_Name(@NotNull boolean active, @NotNull String name_name);

	Iterable<Plans> findByActive_Active(@NotNull boolean active);

	Optional<Plans> findByActive_Active_(@NotNull boolean active);

	Optional<Plans> findByPromoted_Promoted(@NotNull boolean promoted);
	@Modifying
	@Query("UPDATE Plans p SET p.deleted = true WHERE p = :plan AND p.version = :desiredVersion")
	int ceaseByPlan(@Param("plan") Plans plan, @Param("desiredVersion") long desiredVersion);
}

interface PlansRepoHttpCustom{
	HttpResponse<String> getPlansFromOtherAPI(String name, String auth) throws URISyntaxException, IOException, InterruptedException;
	HttpResponse<String> getPlansFromOtherAPI() throws URISyntaxException, IOException, InterruptedException;

	HttpResponse<String> doPlansPatchAPI(String name, final long desiredVersion, String auth,String json) throws URISyntaxException, IOException, InterruptedException;
	HttpResponse<String> doPlansPatchMoneyAPI(String name, final long desiredVersion,String auth, String json)throws URISyntaxException, IOException, InterruptedException;
	HttpResponse<String> doPlansPatchDeactivate(String name, String auth,final long desiredVersion)throws URISyntaxException, IOException, InterruptedException;

	Iterable<Plans> addLocalPlusNot(Iterable<Plans> planslocal) throws URISyntaxException, IOException, InterruptedException;

	Plans updateNotLocal( EditPlansRequest resource, String name, long desiredVersion, String auth) throws URISyntaxException, IOException, InterruptedException;

	Plans createNotLocal(String auth, CreatePlanRequest resource) throws URISyntaxException, IOException, InterruptedException;

	Plans deactivateNotLocal( String name, long desiredVersion, String authorizationToken) throws URISyntaxException, IOException, InterruptedException;

	Optional<Plans> getPlanByNameNotLocally(String planName) throws IOException, InterruptedException, URISyntaxException;
}
@RequiredArgsConstructor
@Configuration
class PlansRepoHttpCustomImpl implements PlansRepoHttpCustom {
	@Value("${plans.external}")
	private String externalPlansUrl;
	@Value("${plansall.external}")
	private String externalAllPlansUrl;
	@Value("${server.port}")
	private int currentPort;
	private final CreatePlansMapper createPlansMapper;
	private final PlansMapperInverse plansMapperInverse;

	@Override
	public HttpResponse<String> getPlansFromOtherAPI(String name, String auth) throws URISyntaxException, IOException, InterruptedException {


		String urlWithDynamicName = externalPlansUrl.replace("{name}", name);
		URI uri = new URI(urlWithDynamicName);

		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.GET()
				//.header("Authorization",auth)
				.build();

		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		return response;

	}


	@Override
	public HttpResponse<String> getPlansFromOtherAPI() throws URISyntaxException, IOException, InterruptedException {

		URI uri = new URI(externalPlansUrl);
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.GET()
				.build();

		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		return response;
	}



	@Override
	public HttpResponse<String> doPlansPatchMoneyAPI(String name, final long desiredVersion, String auth,String json) throws URISyntaxException, IOException, InterruptedException {
		int otherPort = (currentPort == 8081) ? 8090 : 8081;
		String apiUrl = "http://localhost:" + otherPort + "/api/plans/updateMoney/" + name;
		HttpRequest requestpatch = HttpRequest.newBuilder()
				.uri(URI.create(apiUrl))
				.header("Content-Type", "application/json")
				.header("if-match", Long.toString(desiredVersion))
				.header("Authorization",auth)
				.method("PATCH", HttpRequest.BodyPublishers.ofString(json))
				.build();
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpResponse<String> responses = httpClient.send(requestpatch, HttpResponse.BodyHandlers.ofString());

		return responses;

	}

	@Override
	public HttpResponse<String> doPlansPatchAPI(String name, final long desiredVersion, String auth,String json) throws URISyntaxException, IOException, InterruptedException {
		int otherPort = (currentPort == 8081) ? 8090 : 8081;
		String apiUrl = "http://localhost:" + otherPort + "/api/plans/update/" + name;

		HttpRequest requestpatch = HttpRequest.newBuilder()
				.uri(URI.create(apiUrl))
				.header("Content-Type", "application/json")
				.header("Authorization",auth)
				.header("if-match", Long.toString(desiredVersion) )
				.method("PATCH", HttpRequest.BodyPublishers.ofString(json))
				.build();
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpResponse<String> responses = httpClient.send(requestpatch, HttpResponse.BodyHandlers.ofString());

		return responses;

	}

	@Override
	public HttpResponse<String> doPlansPatchDeactivate(String name, String auth,long desiredVersion) throws URISyntaxException, IOException, InterruptedException {
		int otherPort = (currentPort == 8081) ? 8090 : 8081;
		String apiUrl = "http://localhost:" + otherPort + "/api/plans/deactivate/" + name;

		HttpRequest requestpatch = HttpRequest.newBuilder()
				.uri(URI.create(apiUrl))
				.header("Content-Type", "application/json")
				.header("Authorization",auth)
				.header("if-match", Long.toString(desiredVersion))
				.method("PATCH", HttpRequest.BodyPublishers.noBody())
				.build();
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpResponse<String> responses = httpClient.send(requestpatch, HttpResponse.BodyHandlers.ofString());

		return responses;
	}

	@Override
	public Iterable<Plans> addLocalPlusNot(Iterable<Plans> planslocal) throws URISyntaxException, IOException, InterruptedException {
		HttpResponse<String> response = getPlansFromOtherAPI();

		JSONArray jsonArray = new JSONArray(response.body());
		for (int i = 0; i < jsonArray.length(); i++) {
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
		return planslocal;
	}

	@Override
	public Plans updateNotLocal( EditPlansRequest resource, String name, long desiredVersion, String auth) throws URISyntaxException, IOException, InterruptedException {
		HttpResponse<String> response = getPlansFromOtherAPI(name,auth);

		if (response.statusCode() == 200) {
			Gson gson = new Gson();
			String json = gson.toJson(resource);
			HttpResponse<String> responses = doPlansPatchAPI(name,desiredVersion,auth,json);

			if(responses.statusCode() == 200){
				//
				PlanRequest planRequest2 = gson.fromJson(responses.body(), PlanRequest.class);
				//
				return plansMapperInverse.toPlansView(planRequest2);
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
	public Plans createNotLocal( String auth, CreatePlanRequest resource) throws URISyntaxException, IOException, InterruptedException {
		HttpResponse<String> response = getPlansFromOtherAPI(resource.getName(),auth);
		if(response.statusCode() == 404){
			Plans obj = createPlansMapper.create(resource);
			return obj;
		}else {
			throw new IllegalArgumentException("Plan with name " + resource.getName() + " already exists on another machine!");
		}
	}

	@Override
	public Plans deactivateNotLocal( String name, long desiredVersion, String authorizationToken) throws URISyntaxException, IOException, InterruptedException {
		HttpResponse<String> response = getPlansFromOtherAPI(name,authorizationToken);

		if (response.statusCode() == 200) {
			Gson gson = new Gson();
			HttpResponse<String> responses=doPlansPatchDeactivate(name,authorizationToken,desiredVersion);
			if(responses.statusCode() == 200){
				PlanRequest planRequest = gson.fromJson(responses.body(), PlanRequest.class);
				return plansMapperInverse.toPlansView(planRequest);
			}
		}
		else{
			throw new IllegalArgumentException("Plan with name " + name + " does not exists on another machine and locally !");
		}
		return null;
	}

	@Override
	public Optional<Plans> getPlanByNameNotLocally(String planName) throws IOException, InterruptedException, URISyntaxException {
		HttpResponse<String> plan = getPlansFromOtherAPI(planName,"auth");


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

	private Iterable<Plans> addPlanToIterable(Iterable<Plans> plans, Plans newPlan) {
		if (plans instanceof List) {
			List<Plans> planList = new ArrayList<>((List<Plans>) plans);
			planList.add(newPlan);
			return planList;
		}
		return plans;
	}
}



