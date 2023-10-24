package com.example.psoft_22_23_project.plansmanagement.services;

import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepoHttp;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.Optional;



@Configuration
@RequiredArgsConstructor
@Getter
public class RepoManager {
    private final PlansRepository repository;
    private final PlansRepoHttp plansRepoHttp;

    public Plans save(Plans obj) {
        return repository.save(obj);
    }

    public Iterable<Plans> findByActive_Active() {return repository.findByActive_Active(true);}

    public Optional<Plans> findByName_Name(String planName) {
        return repository.findByName_Name(planName);
    }
    public Optional<Plans> findByPromoted_Promoted(boolean b) {
        return repository.findByPromoted_Promoted(b);
    }
    public int ceaseByPlan(Plans plans, long desiredVersion) {
        return repository.ceaseByPlan(plans,desiredVersion);
    }

    public Optional<Plans> findByActive_ActiveAndName_Name(Boolean active, String name) {
        return repository.findByActive_ActiveAndName_Name(active,name);
    }

    public Optional<Plans> findByActive_ActiveAndName_Name_AndPromoted_Promoted(Boolean active, String name, Boolean promoted) {
        return repository.findByActive_ActiveAndName_Name_AndPromoted_Promoted(active,name,promoted);
    }

    public HttpResponse<String> getPlansFromOtherAPI() throws URISyntaxException, IOException, InterruptedException {
        return plansRepoHttp.getPlansFromOtherAPI();
    }
    public HttpResponse<String> getPlansFromOtherAPI(String planName) throws URISyntaxException, IOException, InterruptedException {
        return plansRepoHttp.getPlansFromOtherAPI(planName);
    }
    public HttpResponse<String> doPlansPatchAPI(String name, long desiredVersion, String json) throws URISyntaxException, IOException, InterruptedException {
        return plansRepoHttp.doPlansPatchAPI(name,desiredVersion,json);
    }

    public HttpResponse<String> doPlansPatchMoneyAPI(String name, long desiredVersion, String json) throws URISyntaxException, IOException, InterruptedException {
        return plansRepoHttp.doPlansPatchMoneyAPI(name,desiredVersion,json);
    }

    public HttpResponse<String> doPlansPatchDeactivate(String name, long desiredVersion) throws URISyntaxException, IOException, InterruptedException {
        return plansRepoHttp.doPlansPatchDeactivate(name,desiredVersion);
    }

}
