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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface PlansRepoHttpCustom {
    HttpResponse<String> getPlansFromOtherAPI(String name, String auth) throws URISyntaxException, IOException, InterruptedException;

    HttpResponse<String> getPlansFromOtherAPI() throws URISyntaxException, IOException, InterruptedException;

    HttpResponse<String> doPlansPatchAPI(String name, final long desiredVersion, String auth, String json) throws URISyntaxException, IOException, InterruptedException;

    HttpResponse<String> doPlansPatchMoneyAPI(String name, final long desiredVersion, String auth, String json) throws URISyntaxException, IOException, InterruptedException;

    HttpResponse<String> doPlansPatchDeactivate(String name, String auth, final long desiredVersion) throws URISyntaxException, IOException, InterruptedException;

    Iterable<Plans> addLocalPlusNot(Iterable<Plans> planslocal) throws URISyntaxException, IOException, InterruptedException;

    Plans updateNotLocal(EditPlansRequest resource, String name, long desiredVersion, String auth) throws URISyntaxException, IOException, InterruptedException;

    Plans createNotLocal(String auth, CreatePlanRequest resource) throws URISyntaxException, IOException, InterruptedException;

    Plans deactivateNotLocal(String name, long desiredVersion, String authorizationToken) throws URISyntaxException, IOException, InterruptedException;

    Optional<Plans> getPlanByNameNotLocally(String planName) throws IOException, InterruptedException, URISyntaxException;
}
@RequiredArgsConstructor
@Configuration
class PlansRepoHttpCustomImpl implements PlansRepoHttpCustom {
    @Value("${server.port}")
    private int currentPort;
    @Value("${port1}")
    private int portOne;
    @Value("${port2}")
    private int portTwo;

    @Value("${plans.external}")
    private String externalPlansUrl;
    @Value("${plansall.external}")
    private String externalAllPlansUrl;
    @Value("${patch.details}")
    private String patchDetails;
    @Value("${patch.deactivate}")
    private String patchDeactivate;
    private int otherPort;
    private final CreatePlansMapper createPlansMapper;
    private final PlansMapperInverse plansMapperInverse;

    @Override
    public HttpResponse<String> getPlansFromOtherAPI(String name, String auth) throws URISyntaxException, IOException, InterruptedException {
        //otherPort = (currentPort==portTwo) ? portOne : portTwo;
        //URI uri = new URI("http://localhost:" + otherPort + "/api/plans/external/"+name);

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
        //otherPort = (currentPort==portTwo) ? portOne : portTwo;
        //URI uri = new URI("http://localhost:" + otherPort + "/api/plans/external");
        URI uri = new URI(externalAllPlansUrl);
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
        //String urlWithDynamicName = patchDetails.replace("{name}", name);
        //URI uri = new URI(urlWithDynamicName);

        otherPort = (currentPort==portTwo) ? portOne : portTwo;
        URI uri = new URI("http://localhost:" + otherPort + "/api/plans/update/" + name);

        HttpRequest requestpatch = HttpRequest.newBuilder()
                .uri(uri)
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
        otherPort = (currentPort==portTwo) ? portOne : portTwo;
        URI uri = new URI("http://localhost:" + otherPort + "/api/plans/deactivate/" + name);
        //String urlWithDynamicName = patchDeactivate.replace("{name}", name);
        //URI uri = new URI(urlWithDynamicName);

        HttpRequest requestpatch = HttpRequest.newBuilder()
                .uri(uri)
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

