package com.example.psoft_22_23_project.subscriptionsmanagement.repositories;


import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.CreateSubscriptionsMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
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

@Repository
@Configuration
public interface SubscriptionsRepository extends SubscriptionsRepositoryDB,SubsRepoHttpCustom{

}
interface SubscriptionsRepositoryDB extends CrudRepository<Subscriptions,Long> {
    Optional<Subscriptions> findById(long id);
    Optional<Subscriptions> findByActiveStatus_ActiveAndUser(@NotNull boolean active, @NotNull String user);

}
interface SubsRepoHttpCustom {
    Subscriptions planExists(HttpResponse<String> plan, String auth, CreateSubscriptionsRequest resource) throws URISyntaxException, IOException, InterruptedException;

    HttpResponse<String> getPlansFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException;

    HttpResponse<String> getUserFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException;

    HttpResponse<String> getSubsFromOtherApi(String name,String auth) throws URISyntaxException, IOException, InterruptedException;

    PlansDetails subExistNotLocal(String username, String auth) throws URISyntaxException, IOException, InterruptedException;

    Subscriptions cancelSub(String newString, String auth, HttpResponse<String> user, long desiredVersion) throws URISyntaxException, IOException, InterruptedException;

}
@RequiredArgsConstructor
@Configuration
class SubsRepoHttpCustomImpl implements SubsRepoHttpCustom {
    @Value("${server.port}")
    private int currentPort;
    private final CreateSubscriptionsMapper createSubscriptionsMapper;
    private final SubscriptionsRepositoryDB subscriptionsRepositoryDB;


    @Override
    public Subscriptions planExists(HttpResponse<String> plan, String auth, CreateSubscriptionsRequest resource) throws URISyntaxException, IOException, InterruptedException {
        if (plan.statusCode() == 200) {
            JSONObject jsonArray = new JSONObject(plan.body());

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            int commaIndex = username.indexOf(",");
            String newString;
            if (commaIndex != -1) {
                newString = username.substring(commaIndex + 1);
            } else {
                newString = username;
            }

            HttpResponse<String> user = getUserFromOtherAPI(newString);
            if (user.statusCode() == 200) {
                Optional<Subscriptions> existingSubscription = subscriptionsRepositoryDB.findByActiveStatus_ActiveAndUser(true, newString);

                if (existingSubscription.isPresent()) {
                    if (existingSubscription.get().getActiveStatus().isActive()) {
                        throw new IllegalArgumentException("You need to let your active subscription end in order to subscribe, locally ");
                    }
                } else {
                    HttpResponse<String> existingSubscription2 = getSubsFromOtherApi(newString,auth);
                    if (existingSubscription2.statusCode() == 404) {
                        Subscriptions obj = createSubscriptionsMapper.create(newString, jsonArray.getString("name"), resource);
                        return obj;
                    } else
                        throw new IllegalArgumentException("You need to let your active subscription end in order to subscribe, not locally ");
                }
            } else throw new IllegalArgumentException("User With Username: "+newString+" Does Not Exist");
        } else throw new IllegalArgumentException("Plan With Name "+ resource.getName() +" Does Not Exist");

        return null;
    }

    @Override
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

    @Override
    public HttpResponse<String> getUserFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException {
        // 82 91 subs
        // 81 90 plans
        // 83 92 users
        int otherPort = (currentPort == 8082) ? 8083 : 8092;
        URI uri = new URI("http://localhost:" + otherPort + "/api/user/" + name);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;

    }

    @Override
    public HttpResponse<String> getSubsFromOtherApi(String userName,String auth) throws URISyntaxException, IOException, InterruptedException {
        // 82 91 subs
        // 81 90 plans
        // 83 92 users
        int otherPort = (currentPort == 8082) ? 8091 : 8082;

        URI uri = new URI("http://localhost:" + otherPort + "/api/subscriptions/external/" + userName);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Authorization",auth)
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;

    }

    @Override
    public PlansDetails subExistNotLocal(String username, String auth) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> existingSubscription2 = getSubsFromOtherApi(username,auth);
        JSONObject planjsonArray2 = new JSONObject(existingSubscription2.body());

        if (existingSubscription2.statusCode() == 200) {
            HttpResponse<String> plan = getPlansFromOtherAPI(planjsonArray2.getString("planName"));
            JSONObject planjsonArray = new JSONObject(plan.body());
            return new PlansDetails(planjsonArray.getString("name"),
                    planjsonArray.getString("description"),
                    planjsonArray.getString("numberOfMinutes"),
                    planjsonArray.getString("maximumNumberOfUsers"),
                    planjsonArray.getString("musicCollection"),
                    planjsonArray.getString("musicSuggestion"),
                    planjsonArray.getString("annualFee"),
                    planjsonArray.getString("monthlyFee"),
                    planjsonArray.getString("active"),
                    planjsonArray.getString("promoted"));
        }else throw new IllegalArgumentException("User Does not have subs");
    }

    @Override
    public Subscriptions cancelSub(String newString, String auth, HttpResponse<String> user, long desiredVersion) throws URISyntaxException, IOException, InterruptedException {
        if (user.statusCode() == 200) {
            Optional<Subscriptions> existingSubscription = subscriptionsRepositoryDB.findByActiveStatus_ActiveAndUser(true, newString);
            if (existingSubscription.isPresent()) {
                Subscriptions subscription = existingSubscription.get();
                subscription.deactivate(desiredVersion);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate startDate = LocalDate.parse(subscription.getStartDate().getStartDate(), formatter);


                if (Objects.equals(subscription.getPaymentType().getPaymentType(), "monthly")) {
                    if (startDate.getMonthValue() == LocalDate.now().getMonthValue()) {
                        if (startDate.getYear() != LocalDate.now().getYear()) {
                            subscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1).plusYears(LocalDate.now().getYear() - startDate.getYear())));
                        } else {
                            subscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1)));
                        }

                    } else if (startDate.getDayOfMonth() >= LocalDate.now().getDayOfMonth()) {
                        if (startDate.getYear() != LocalDate.now().getYear()) {
                            subscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1).plusYears(LocalDate.now().getYear() - startDate.getYear())));
                        } else {
                            subscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1)));
                        }

                    } else {
                        if (startDate.getYear() != LocalDate.now().getYear()) {
                            subscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths((LocalDate.now().getMonthValue() - startDate.getMonthValue()) + 1).plusYears(LocalDate.now().getYear() - startDate.getYear())));
                        } else {
                            subscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths((LocalDate.now().getMonthValue() - startDate.getMonthValue()) + 1)));
                        }
                    }
                }
                return subscription;

            } else {
                HttpResponse<String> existingSubscription2 = getSubsFromOtherApi(newString,auth);
                if (existingSubscription2.statusCode() == 200) {
                    throw new IllegalArgumentException("The subscription you want to cancel exists on another machine");
                }
                else throw new IllegalArgumentException("The subscription you want to cancel exists on another machine or locally");

            }
        }throw new IllegalArgumentException("User Does not Exist");

    }
}


