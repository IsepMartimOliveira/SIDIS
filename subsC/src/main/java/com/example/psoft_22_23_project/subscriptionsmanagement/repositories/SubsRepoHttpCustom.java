package com.example.psoft_22_23_project.subscriptionsmanagement.repositories;

import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.CreateSubscriptionsMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public interface SubsRepoHttpCustom {


    Optional<PlansDetails> findPlan(String name) throws URISyntaxException, IOException, InterruptedException;
}

@RequiredArgsConstructor
@Configuration
class SubsRepoHttpCustomImpl implements SubsRepoHttpCustom {

    @Value("${port1}")
    private int portOne;
    @Value("${port2}")
    private int portTwo;

    @Override
    public Optional<PlansDetails> findPlan(String planName) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> plan = getPlansFromOtherAPI(planName);
        if(plan.statusCode() == 200){
            JSONObject planjsonArray = new JSONObject(plan.body());
            PlansDetails plansDetails = new PlansDetails(
                    planjsonArray.getString("name"),
                    planjsonArray.getString("description"),
                    planjsonArray.getString("numberOfMinutes"),
                    planjsonArray.getString("maximumNumberOfUsers"),
                    planjsonArray.getString("musicCollection"),
                    planjsonArray.getString("musicSuggestion"),
                    planjsonArray.getString("annualFee"),
                    planjsonArray.getString("monthlyFee"),
                    planjsonArray.getString("active"),
                    planjsonArray.getString("promoted"));
                    return Optional.of(plansDetails);
        }
        throw new IllegalArgumentException("Plan with name : "+planName +" does not exist!");
    }

    public HttpResponse<String> getPlansFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException {
        // 82 91 subs
        // 81 90 plans
        // 83 92 users
        HttpClient client = HttpClient.newHttpClient();
        try {
            URI uri = new URI("http://localhost:" + portOne + "/api/plans/" + name);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            URI uri2 = new URI("http://localhost:" + portTwo + "/api/plans/" + name);
            HttpRequest request2 = HttpRequest.newBuilder()
                    .uri(uri2)
                    .GET()
                    .build();

            HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
            return response2;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (HttpResponse<String>) HttpResponse.BodyHandlers.ofString();

    }

}

