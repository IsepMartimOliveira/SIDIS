package com.example.psoft_22_23_project.subscriptionsmanagement.repositories;

import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.CreateSubscriptionsMapper;
import com.example.psoft_22_23_project.subscriptionsmanagement.services.SubsManager;
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
    Subscriptions create(String auth, CreateSubscriptionsRequest resource) throws URISyntaxException, IOException, InterruptedException;

    HttpResponse<String> getPlansFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException;

    HttpResponse<String> getSubsFromOtherApi(String name,String auth) throws URISyntaxException, IOException, InterruptedException;

    PlansDetails subExistNotLocal( String auth) throws URISyntaxException, IOException, InterruptedException;

    Subscriptions cancelSub(String auth, long desiredVersion) throws URISyntaxException, IOException, InterruptedException;

    Subscriptions renewSub( String auth, long desiredVersion) throws URISyntaxException, IOException, InterruptedException;

    Subscriptions changePlan( String auth, String name, long desiredVersion) throws URISyntaxException, IOException, InterruptedException;

    Optional<Subscriptions> isPresent(String auth, String newString) throws URISyntaxException, IOException, InterruptedException;

    PlansDetails subExistLocal(String plan) throws URISyntaxException, IOException, InterruptedException;

    Optional<Subscriptions> getSubsByNameNotLocally(String name,String auth) throws IOException, InterruptedException, URISyntaxException;

    Optional<PlansDetails> getPlanByName(String name) throws IOException, InterruptedException, URISyntaxException;
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
    public Subscriptions create( String auth, CreateSubscriptionsRequest resource) throws URISyntaxException, IOException, InterruptedException {
            // verificar se plan existe
            Optional<PlansDetails> plan = getPlanByName(resource.getName());

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            int commaIndex = username.indexOf(",");
            String newString;
            if (commaIndex != -1) {
                newString = username.substring(commaIndex + 1);
            } else {
                newString = username;
            }
            //newString = username
            Optional<Subscriptions> existingSubscription = subscriptionsRepositoryDB.findByActiveStatus_ActiveAndUser(true, newString);
            //ver se user tem subs ativa localmente-> nao pode sub tendo uma sub
            if (existingSubscription.isPresent()) {
                if (existingSubscription.get().getActiveStatus().isActive()) {
                    throw new IllegalArgumentException("You need to let your active subscription end in order to subscribe, locally ");
                }
            }else {
                //ver se user tem subs ativa nao localmente-> nao pode sub tendo uma sub
                HttpResponse<String> existingSubscription2 = getSubsFromOtherApi(newString,auth);
                if (existingSubscription2.statusCode() == 404) {
                    Subscriptions obj = createSubscriptionsMapper.create(newString, plan.get().getName(), resource);
                    return obj;
                }else throw new IllegalArgumentException("You need to let your active subscription end in order to subscribe, not locally ");
            }
        throw new IllegalArgumentException("User does not have sub!");
    }
    @Override
    public Optional<PlansDetails> getPlanByName(String name) throws IOException, InterruptedException, URISyntaxException {
        HttpResponse<String> plan = getPlansFromOtherAPI(name);
        JSONObject planjsonArray = new JSONObject(plan.body());
        if (plan.statusCode() == 200) {
            return Optional.of(new PlansDetails(planjsonArray.getString("name"),
                    planjsonArray.getString("description"),
                    planjsonArray.getString("numberOfMinutes"),
                    planjsonArray.getString("maximumNumberOfUsers"),
                    planjsonArray.getString("musicCollection"),
                    planjsonArray.getString("musicSuggestion"),
                    planjsonArray.getString("annualFee"),
                    planjsonArray.getString("monthlyFee"),
                    planjsonArray.getString("active"),
                    planjsonArray.getString("promoted")));
        }
        throw new IllegalArgumentException("Plan with name:"+ name+" does not exist!");
    }

    @Override
    public HttpResponse<String> getSubsFromOtherApi(String userName,String auth) throws URISyntaxException, IOException, InterruptedException {
        // 82 91 subs
        // 81 90 plans
        // 83 92 users
        String urlWithDynamicName = externalSubscriptionUrl.replace("{username}", userName);
        URI uri = new URI(urlWithDynamicName);
        // URI uri = new URI("http://localhost:" + otherPort + "/api/subscriptions/external/" + userName);
        //otherPort = (currentPort==portTwo) ? portOne : portTwo;
        //URI uri = new URI("http://localhost:" + otherPort + "/api/subscriptions/external/"+userName);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Authorization", auth)
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;

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
    public PlansDetails subExistNotLocal(String auth) throws URISyntaxException, IOException, InterruptedException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int commaIndex = username.indexOf(",");
        String newString;
        if (commaIndex != -1) {
            newString = username.substring(commaIndex + 1);
        } else {
            newString = username;
        }

        HttpResponse<String> existingSubscription2 = getSubsFromOtherApi(newString,auth);
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
        } else throw new IllegalArgumentException("User Does not have subs");
    }

    @Override
    public Subscriptions cancelSub(String auth, long desiredVersion) throws URISyntaxException, IOException, InterruptedException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        int commaIndex = username.indexOf(",");
        String newString;
        if (commaIndex != -1) {
            newString = username.substring(commaIndex + 1);
        } else {
            newString = username;
        }

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
                    throw new IllegalArgumentException("The subscription you want to cancel exists on another machine!");
                } else
                    throw new IllegalArgumentException("The subscription you want to cancel does not exist!");

            }
    }

    @Override
    public Subscriptions renewSub(String auth, long desiredVersion) throws URISyntaxException, IOException, InterruptedException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int commaIndex = username.indexOf(",");

        String newString;
        if (commaIndex != -1) {
            newString = username.substring(commaIndex + 1);
        } else {
            newString = username;
        }

            Optional<Subscriptions> existingSubscription = subscriptionsRepositoryDB.findByActiveStatus_ActiveAndUser(true, newString);
            if (existingSubscription.isPresent()) {
                Subscriptions subscription = existingSubscription.get();
                if (Objects.equals(subscription.getPaymentType().getPaymentType(), "monthly")) {

                    throw new IllegalArgumentException("You can not renew a monthly subscription");
                } else {

                    subscription.checkChange(desiredVersion);


                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate endDate = LocalDate.parse(subscription.getEndDate().getEndDate(), formatter);

                    subscription.getEndDate().setEndDate(String.valueOf(endDate.plusYears(1)));
                }
                return subscription;

            } else {
                HttpResponse<String> existingSubscription2 = getSubsFromOtherApi(newString,auth);
                if (existingSubscription2.statusCode() == 200) {
                    throw new IllegalArgumentException("The subscription you want to renew exists on another machine");

                }
        }
        throw new IllegalArgumentException("User does not have subscription!");
    }

    @Override
    public Subscriptions changePlan( String auth, String name, long desiredVersion) throws URISyntaxException, IOException, InterruptedException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int commaIndex = username.indexOf(",");

        String newString;
        if (commaIndex != -1) {
            newString = username.substring(commaIndex + 1);
        } else {
            newString = username;
        }
            Optional<Subscriptions> existingSubscription = subscriptionsRepositoryDB.findByActiveStatus_ActiveAndUser(true, newString);
            if (existingSubscription.isPresent()) {
                Subscriptions subscription = existingSubscription.get();
                HttpResponse<String> plan = getPlansFromOtherAPI(name);
                JSONObject jsonArray = new JSONObject(plan.body());
                if (Objects.equals(subscription.getPlan(), jsonArray.getString("name"))) {
                    throw new IllegalArgumentException("The user is already subscribed to this plan!");
                }
                if (plan.statusCode() == 200) {
                    subscription.changePlan(desiredVersion, jsonArray.getString("name"));
                    return subscription;
                }
            }else {
                HttpResponse<String> existingSubscription2 = getSubsFromOtherApi(newString,auth);
                if (existingSubscription2.statusCode() == 200) {
                    throw new IllegalArgumentException("The subscription you want to change exists on another machine");
                }

            }
        throw new IllegalArgumentException("User does not have subscription!");
    }

    @Override
    public Optional<Subscriptions> isPresent(String auth, String newString) throws URISyntaxException, IOException, InterruptedException {
        Optional<Subscriptions> subscription = subscriptionsRepositoryDB.findByActiveStatus_ActiveAndUser(true, newString);
        return subscription;
    }

    @Override
    public PlansDetails subExistLocal(String plan2) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> plan = getPlansFromOtherAPI(plan2);
        JSONObject planjsonArray = new JSONObject(plan.body());
        if (plan.statusCode() == 200) {
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

        }
        return  null;
    }

    @Override
    public Optional<Subscriptions> getSubsByNameNotLocally(String name,String auth) throws IOException, InterruptedException, URISyntaxException {
        HttpResponse<String> sub = getSubsFromOtherApi(name,auth);
        //buscar a sub ao rep fora
        if (sub.statusCode() == 200) {
            JSONObject subjsonArray = new JSONObject(sub.body());
            CreateSubscriptionsRequest request = new CreateSubscriptionsRequest(subjsonArray.getString("planName"),subjsonArray.getString("paymentType"));
            Subscriptions subwithplan = createSubscriptionsMapper.create(name,subjsonArray.getString("planName"),request);
            return Optional.ofNullable(subwithplan);
        }
        return null;
    }


}

