package com.example.psoft_22_23_project.plansmanagement.services;

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import com.example.psoft_22_23_project.plansmanagement.api.EditPlansRequest;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepoHttpCustom;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepositoryDB;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class PlansManager {
    private final PlansRepositoryDB dbRepository;
    private final PlansRepoHttpCustom httpRepository;

    @Transactional
    public Optional<Plans> findByName(String name) throws IOException, URISyntaxException, InterruptedException {
        // local db
        Optional<Plans> resultFromDB = dbRepository.findByName_Name(name);

        if (resultFromDB != null) {
            return resultFromDB;
        } else {
            // nao localemente
            Optional<Plans> resultFromHTTP = httpRepository.getPlanByNameNotLocally(name);

            return resultFromHTTP;
        }
    }

    public Iterable<Plans> findByActive_Active(boolean b) {
        return dbRepository.findByActive_Active(b);
    }

    public Iterable<Plans> addLocalPlusNot(Iterable<Plans> planslocal) throws URISyntaxException, IOException, InterruptedException {
        return httpRepository.addLocalPlusNot(planslocal);
    }

    public Optional<Plans> getPlanByNameNotLocally(String planName) throws IOException, URISyntaxException, InterruptedException {
        return httpRepository.getPlanByNameNotLocally(planName);
    }

    public Plans createNotLocal(String auth, CreatePlanRequest resource) throws URISyntaxException, IOException, InterruptedException {
        return httpRepository.createNotLocal(auth,resource);
    }

    public Plans save(Plans obj) {
        return dbRepository.save(obj);
    }

    public Plans updateNotLocal(EditPlansRequest resource, String name, long desiredVersion, String auth) throws URISyntaxException, IOException, InterruptedException {
        return httpRepository.updateNotLocal(resource,name,desiredVersion,auth);
    }

    public Plans deactivateNotLocal(String name, long desiredVersion, String authorizationToken) throws URISyntaxException, IOException, InterruptedException {
        return httpRepository.deactivateNotLocal(name,desiredVersion,authorizationToken);
    }


}