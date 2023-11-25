package com.example.psoft_22_23_project.plansmanagement.repositories;

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import com.example.psoft_22_23_project.plansmanagement.api.PlanRequest;
import com.example.psoft_22_23_project.plansmanagement.api.PlansMapperInverse;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.services.CreatePlansMapper;
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
     PlanRequest getAllExternal() throws URISyntaxException, IOException, InterruptedException;
    Optional<Plans> getPlanByNameNotLocally(String planName) throws IOException, InterruptedException, URISyntaxException;
    Iterable<Plans> addPlanToIterable(Iterable<Plans> plans, Plans newPlan);
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
    private int otherPort;
    private final PlansMapperInverse plansMapperInverse;
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
        }
        return null;
    }
    public Iterable<Plans> addPlanToIterable(Iterable<Plans> plans, Plans newPlan) {
        List<Plans> combinedPlans = new ArrayList<>();
        plans.forEach(combinedPlans::add);
        if (newPlan != null) {
            combinedPlans.add(newPlan);
        }
        return combinedPlans;
    }
    public HttpResponse<String> getPlansFromOtherAPI(String name, String auth) throws URISyntaxException, IOException, InterruptedException {

        otherPort = (currentPort==portTwo) ? portOne : portTwo;
        URI uri = new URI("http://localhost:" + otherPort + "/api/plans/external/"+name);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                //.header("Authorization",auth)
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;

    }

    public HttpResponse<String> getPlansFromOtherAPI() throws URISyntaxException, IOException, InterruptedException {
        otherPort = (currentPort==portTwo) ? portOne : portTwo;
        URI uri = new URI("http://localhost:" + otherPort + "/api/plans/external");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }
    @Override
    public PlanRequest getAllExternal() throws URISyntaxException, IOException, InterruptedException {
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
            return  newPlan;
        }
        return null;
    }



}

