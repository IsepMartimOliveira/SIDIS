package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.subscriptionsmanagement.api.CreateSubscriptionsRequest;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.repositories.SubscriptionsRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionsServiceImpl implements SubscriptionsService {

    private final SubscriptionsRepository subscriptionsRepository;
    private final CreateSubscriptionsMapper createSubscriptionsMapper;
    @Override
    public Iterable<Subscriptions> findAll() {
        return null;
    }

    @Override
    public void migrateAllToPlan(long desiredVersion, String actualPlan, String newPlan) {

    }

    @Override
    public Optional<Subscriptions> findSubByUserExternal(String user) {
        return subscriptionsRepository.findByActiveStatus_ActiveAndUser(true, user);
    }
    @Override
    public Optional<Subscriptions> findSubByUser( String user) {
        return subscriptionsRepository.findByActiveStatus_ActiveAndUser(true, user);
    }

    @Override
    public Subscriptions create(final CreateSubscriptionsRequest resource) throws URISyntaxException, IOException, InterruptedException {

        HttpResponse<String> plan = subscriptionsRepository.getPlansFromOtherAPI(resource.getName());

        if (plan.statusCode() == 200) {
            JSONObject jsonArray = new JSONObject(plan.body());

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Authentication principal = SecurityContextHolder.getContext().getAuthentication();
            //token in principal

            int commaIndex = username.indexOf(",");
            String newString;
            if (commaIndex != -1) {
                newString = username.substring(commaIndex + 1);
            } else {
                newString = username;
            }

            HttpResponse<String> user = subscriptionsRepository.getUserFromOtherAPI(newString);

            if (user.statusCode() == 200) {
                Optional<Subscriptions> existingSubscription = subscriptionsRepository.findByActiveStatus_ActiveAndUser(true, newString);

                if (existingSubscription.isPresent()) {
                    if (existingSubscription.get().getActiveStatus().isActive()) {
                        throw new IllegalArgumentException("You need to let your active subscription end in order to subscribe, locally ");
                    }
                } else {
                    HttpResponse<String> existingSubscription2 = subscriptionsRepository.getSubsFromOtherApi(newString);
                    if (existingSubscription2.statusCode() == 404) {
                        Subscriptions obj = createSubscriptionsMapper.create(newString, jsonArray.getString("name"), resource);
                        return subscriptionsRepository.save(obj);
                    } else
                        throw new IllegalArgumentException("You need to let your active subscription end in order to subscribe, not locally ");
                }
            } else throw new IllegalArgumentException("sub name user");
        } else throw new IllegalArgumentException("sub name plan");

        throw new IllegalArgumentException("problem with sub");
    }

    @SneakyThrows
    @Override
    public PlansDetails planDetails() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        int commaIndex = username.indexOf(",");
        String newString;
        if (commaIndex != -1) {
            newString = username.substring(commaIndex + 1);
        } else {
            newString = username;
        }
        //buscar user
        HttpResponse<String> user = subscriptionsRepository.getUserFromOtherAPI(newString);


        JSONObject jsonArray = new JSONObject(user.body());
        //ver se user existe
        if (user.statusCode() == 200) {
            // buscar subs do user localmente
            Optional<Subscriptions> subscription = subscriptionsRepository.findByActiveStatus_ActiveAndUser(true, jsonArray.getString("username"));

            if (subscription.isPresent()) {
                HttpResponse<String> plan = subscriptionsRepository.getPlansFromOtherAPI(subscription.get().getPlan());
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
            } else {
                HttpResponse<String> existingSubscription2 = subscriptionsRepository.getSubsFromOtherApi(jsonArray.getString("username"));
                JSONObject planjsonArray2 = new JSONObject(existingSubscription2.body());

                if (existingSubscription2.statusCode() == 200) {
                    HttpResponse<String> plan = subscriptionsRepository.getPlansFromOtherAPI(planjsonArray2.getString("planName"));
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
                }
            }
        }
        throw new IllegalArgumentException("adasd");
    }

    @Override
    public Subscriptions cancelSubscription(final long desiredVersion) throws URISyntaxException, IOException, InterruptedException {

        // Check if the current user is authorized to cancel the subscription
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        int commaIndex = username.indexOf(",");
        String newString;
        if (commaIndex != -1) {
            newString = username.substring(commaIndex + 1);
        } else {
            newString = username;
        }

        HttpResponse<String> user = subscriptionsRepository.getUserFromOtherAPI(newString);
        if (user.statusCode() == 200) {
            Optional<Subscriptions> existingSubscription = subscriptionsRepository.findByActiveStatus_ActiveAndUser(true, newString);
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
                return subscriptionsRepository.save(subscription);

            } else {
                HttpResponse<String> existingSubscription2 = subscriptionsRepository.getSubsFromOtherApi(newString);
                if (existingSubscription2.statusCode() == 200) {
                    throw new IllegalArgumentException("The subscription you want to cancel exists on another machine");

                }

            }
        }


        throw new IllegalArgumentException("Something is really bad :(");


    }

    @Override
    public Subscriptions renewAnualSubscription(final long desiredVersion) throws URISyntaxException, IOException, InterruptedException {

        // Check if the current user is authorized to cancel the subscription
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int commaIndex = username.indexOf(",");

        String newString;
        if (commaIndex != -1) {
            newString = username.substring(commaIndex + 1);
        } else {
            newString = username;
        }

        HttpResponse<String> user = subscriptionsRepository.getUserFromOtherAPI(newString);
        if (user.statusCode() == 200) {
            Optional<Subscriptions> existingSubscription = subscriptionsRepository.findByActiveStatus_ActiveAndUser(true, newString);
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
                return subscriptionsRepository.save(subscription);

            } else {
                HttpResponse<String> existingSubscription2 = subscriptionsRepository.getSubsFromOtherApi(newString);
                if (existingSubscription2.statusCode() == 200) {
                    throw new IllegalArgumentException("The subscription you want to renew exists on another machine");

                }
            }



        }


        throw new IllegalArgumentException("Something is really bad :(");
    }
    @Override
    public Subscriptions changePlan(final long desiredVersion, final String name) throws URISyntaxException, IOException, InterruptedException {


        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int commaIndex = username.indexOf(",");

        String newString;
        if (commaIndex != -1) {
            newString = username.substring(commaIndex + 1);
        } else {
            newString = username;
        }


        HttpResponse<String> user = subscriptionsRepository.getUserFromOtherAPI(newString);
        if(user.statusCode()==200){
            Optional<Subscriptions> existingSubscription = subscriptionsRepository.findByActiveStatus_ActiveAndUser(true, newString);
            if (existingSubscription.isPresent()) {
                Subscriptions subscription = existingSubscription.get();
                HttpResponse<String> plan = subscriptionsRepository.getPlansFromOtherAPI(name);
                HttpResponse<String> plan2 = subscriptionsRepository.getPlansFromOtherAPI(subscription.getPlan());
                JSONObject initialPlan=new JSONObject(plan2.body());
                JSONObject jsonArray = new JSONObject(plan.body());
                if(Objects.equals(initialPlan.getString("name"), jsonArray.getString("name"))){
                    throw new IllegalArgumentException("The user is already subscribed to this plan!");
                }
                if (plan.statusCode() == 200 ) {

                    subscription.changePlan(desiredVersion, jsonArray.getString("name"));
                    return subscriptionsRepository.save(subscription);
                }


            }else{
                HttpResponse<String> existingSubscription2 = subscriptionsRepository.getSubsFromOtherApi(newString);
                if (existingSubscription2.statusCode() == 200) {
                    throw new IllegalArgumentException("The subscription you want to change exists on another machine");

                }

            }


        }
        throw new IllegalArgumentException("No subscriptions associated with the user");

    }


}
/*
    @Override
    public Iterable<Subscriptions> findAll() {
        return repository.findAll();
    }

    @Override
    public Subscriptions create(final CreateSubscriptionsRequest resource) {

        Plans plan = plansRepository.findByActive_ActiveAndName_Name(true, resource.getName())
                .orElseThrow(() -> new EntityNotFoundException("Plan not found with name " + resource.getName()));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int commaIndex = username.indexOf(",");

        String newString;
        if (commaIndex != -1) {
            newString = username.substring(0, commaIndex);
        } else {
            newString = username;
        }


        User user = userRepository.findById(Long.valueOf(newString))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        Optional <Subscriptions> existingSubscription = repository.findByActiveStatus_ActiveAndUser(true, user);

        if (existingSubscription.isPresent()){
            if (existingSubscription.get().getActiveStatus().isActive()) {
                throw new IllegalArgumentException("You need to let your active subscription end in order to subscribe");
            }
        }




        // construct a new object based on data received by the service
        Subscriptions obj = createSubscriptionsMapper.create(user, plan, resource);


        return repository.save(obj);
    }



    @Override
    public Subscriptions cancelSubscription(final long desiredVersion){

        // Check if the current user is authorized to cancel the subscription
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int commaIndex = username.indexOf(",");

        String newString;
        if (commaIndex != -1) {
            newString = username.substring(0, commaIndex);
        } else {
            newString = username;
        }

        User user = userRepository.findById(Long.valueOf(newString)).orElseThrow(() -> new EntityNotFoundException("User not found with ID " + newString));

        // Check if subscription exists
        Subscriptions subscription = repository.findByActiveStatus_ActiveAndUser(true, user)
                .orElseThrow(() -> new EntityNotFoundException("No subscriptions associated with this user"));



        subscription.deactivate(desiredVersion);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(subscription.getStartDate().getStartDate(), formatter);


        if(Objects.equals(subscription.getPaymentType().getPaymentType(), "monthly")){
            if (startDate.getMonthValue() == LocalDate.now().getMonthValue()){
                if (startDate.getYear() != LocalDate.now().getYear()) {
                    subscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1).plusYears(LocalDate.now().getYear() - startDate.getYear())));
                }else{
                    subscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1)));
                }

            }else if (startDate.getDayOfMonth() >= LocalDate.now().getDayOfMonth()){
                if (startDate.getYear() != LocalDate.now().getYear()){
                    subscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1).plusYears(LocalDate.now().getYear() - startDate.getYear())));
                }else {
                    subscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1)));
                }

            } else {
                if (startDate.getYear() != LocalDate.now().getYear()) {
                    subscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths((LocalDate.now().getMonthValue() - startDate.getMonthValue())+1).plusYears(LocalDate.now().getYear() - startDate.getYear())));
                } else {
                    subscription.getEndDate().setEndDate(String.valueOf(startDate.plusMonths((LocalDate.now().getMonthValue() - startDate.getMonthValue())+1)));
                }
            }
        }
        return repository.save(subscription);
    }

    @SneakyThrows
    @Override
    public PlansDetails planDetails(){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int commaIndex = username.indexOf(",");

        String newString;
        if (commaIndex != -1) {
            newString = username.substring(0, commaIndex);
        } else {
            newString = username;
        }

        User user = userRepository.findById(Long.valueOf(newString)).orElseThrow(() -> new EntityNotFoundException("You need to login in order to check the plan details"));


        Subscriptions subscription = repository.findByActiveStatus_ActiveAndUser(true, user)
                .orElseThrow(() -> new EntityNotFoundException("No subscriptions associated with this user"));


        String body = getFunnyFact();
        JSONObject jsonObject = new JSONObject(body);
        String fact = jsonObject.getString("text");

        String weather = getWeather();


        return new PlansDetails(subscription.getPlan(),fact, weather);
    }

    private int getDay() {
        return LocalDate.now().getDayOfMonth();
    }
    private int getMonth() {
        return LocalDate.now().getMonthValue();
    }

    private String getFunnyFact(){
        int dia = getDay();
        int mes = getMonth();
        HttpResponse<String> response = Unirest.get("https://numbersapi.p.rapidapi.com/"+mes+"/"+dia+"/date?fragment=true&json=true")
                .header("X-RapidAPI-Key", "32ff449f65msh803a8c15c44f9c7p10a029jsn6f2b5da2245c")
                .header("X-RapidAPI-Host", "numbersapi.p.rapidapi.com")
                .asString();
        return response.body();

    }



    private String getWeather() throws JSONException {

        if (apiLocationId() == null){
            return null;
        }else {
            HttpResponse<String> response = Unirest.get("https://foreca-weather.p.rapidapi.com/current/"+apiLocationId()+"?tempunit=C")
                    .header("X-RapidAPI-Key", "f838f573bamsheba21fef17c2ecfp1b1ed0jsna1a6dd4af28e")
                    .header("X-RapidAPI-Host", "foreca-weather.p.rapidapi.com")
                    .asString();

            JSONObject jsonObject = new JSONObject(response.body());
            JSONObject current = jsonObject.getJSONObject("current");
            return current.getString("symbolPhrase");
        }

    }

    private String apiLocationId() throws JSONException {

        if (getLocation() == null){
            return null;
        }else {
            HttpResponse<String> response = Unirest.get("https://foreca-weather.p.rapidapi.com/location/search/"+getLocation())
                    .header("X-RapidAPI-Key", "f838f573bamsheba21fef17c2ecfp1b1ed0jsna1a6dd4af28e")
                    .header("X-RapidAPI-Host", "foreca-weather.p.rapidapi.com")
                    .asString();

            JSONObject locationJsonObject = new JSONObject(response.body());
            JSONArray locations = locationJsonObject.getJSONArray("locations");
            JSONObject city = locations.getJSONObject(0);
            return city.getString("id");
        }

    }

    private String getLocation(){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int commaIndex = username.indexOf(",");

        String newString;
        if (commaIndex != -1) {
            newString = username.substring(0, commaIndex);
        } else {
            newString = username;
        }

        User user = userRepository.findById(Long.valueOf(newString))
                .orElseThrow(() -> new EntityNotFoundException("You need to login"));

        return user.getLocation();

    }

    @Override
    public Subscriptions renewAnualSubscription(final long desiredVersion){

        // Check if the current user is authorized to cancel the subscription
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int commaIndex = username.indexOf(",");

        String newString;
        if (commaIndex != -1) {
            newString = username.substring(0, commaIndex);
        } else {
            newString = username;
        }

        User user = userRepository.findById(Long.valueOf(newString)).orElseThrow(() -> new EntityNotFoundException("User not found with ID " + newString));

        // Check if subscription exists
        Subscriptions subscription = repository.findByActiveStatus_ActiveAndUser(true, user)
                .orElseThrow(() -> new EntityNotFoundException("No subscriptions associated with this user"));

        if (Objects.equals(subscription.getPaymentType().getPaymentType(), "monthly")){

            throw new IllegalArgumentException("You can not renew a monthly subscription");
        } else {

            subscription.checkChange(desiredVersion);


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate endDate = LocalDate.parse(subscription.getEndDate().getEndDate(), formatter);

            subscription.getEndDate().setEndDate(String.valueOf(endDate.plusYears(1)));
        }
        return repository.save(subscription);
    }



    @Override
    public Subscriptions changePlan(final long desiredVersion, final String name) {


        Plans plan = plansRepository.findByActive_ActiveAndName_Name(true, name)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found with name " + name));

        int deviceLimit = plan.getMaximumNumberOfUsers().getMaximumNumberOfUsers();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int commaIndex = username.indexOf(",");

        String newString;
        if (commaIndex != -1) {
            newString = username.substring(0, commaIndex);
        } else {
            newString = username;
        }


        User user = userRepository.findById(Long.valueOf(newString))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));



        Subscriptions subscription = repository.findByActiveStatus_ActiveAndUser(true, user)
                 .orElseThrow(() -> new EntityNotFoundException("No subscriptions associated with this user"));

        int activeDevices= deviceRepository.countBySubscription(subscription);

        if(activeDevices > deviceLimit){
            throw new IllegalArgumentException("The plan you are trying to change has a number of devices lower than the actual number of active devices. Please remove them and try again");
        }


        if(subscription.getPlan().getName().getName().equals(name)){
            throw new IllegalArgumentException("You are already subscribed to this plan");
        }



        subscription.changePlan(desiredVersion, plan);

        return repository.save(subscription);
    }


    @Override
    public void migrateAllToPlan(final long desiredVersion, final String actualPlan, final String newPlan) {

        if (actualPlan.equals(newPlan)) throw new DuplicateRequestException("You are trying to migrate all the users to their actual plan");


        Plans plan_old = plansRepository.findByActive_ActiveAndName_Name(true, actualPlan)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found with name " + actualPlan));

        Plans plan_new = plansRepository.findByActive_ActiveAndName_Name(true, newPlan)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found with name " + newPlan));




        List<Subscriptions> subs = repository.findAllByPlanAndActiveStatus_Active(plan_old, true);

        if (subs.isEmpty()) throw new EntityNotFoundException("No subscriptions associated with plan " + actualPlan);

        for (Subscriptions sub : subs) {
            int deviceLimit = plan_new.getMaximumNumberOfUsers().getMaximumNumberOfUsers();
            int activeDevices = deviceRepository.countBySubscription(sub);

            if (activeDevices > deviceLimit) {
                throw new IllegalArgumentException("There are users with a number of active devices higher than the number of devices allowed in the subscription you are trying to move them. Ask the user to remove them and try again");
            }
        }

        for (Subscriptions sub : subs) {


            sub.changePlan(desiredVersion, plan_new);

            repository.save(sub);
        }
    }

     */








