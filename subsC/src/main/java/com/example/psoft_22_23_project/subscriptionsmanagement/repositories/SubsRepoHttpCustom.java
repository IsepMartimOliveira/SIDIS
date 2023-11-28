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
    @Value("${server.port}")
    private int currentPort;
    @Value("${port1}")
    private int portOne;
    @Value("${port2}")
    private int portTwo;
    private int otherPort;
    @Value("${plan.server.port}")
    private int planPort;
    @Value("${user.server.port}")
    private int userPort;

    @Value("${subscription.external}")
    private String externalSubscriptionUrl;
    private final CreateSubscriptionsMapper createSubscriptionsMapper;
    private final SubscriptionsRepositoryDB subscriptionsRepositoryDB;

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
        int otherPort = (currentPort == 8082) ? 8081 : 8090;
        URI uri = new URI("http://localhost:" + otherPort + "/api/plans/" + name);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;

    }

}

