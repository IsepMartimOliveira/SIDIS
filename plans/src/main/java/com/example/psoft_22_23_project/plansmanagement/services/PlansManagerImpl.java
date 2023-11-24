package com.example.psoft_22_23_project.plansmanagement.services;

import com.example.psoft_22_23_project.plansmanagement.api.EditPlansRequest;
import com.example.psoft_22_23_project.plansmanagement.api.PlanRequest;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepoHttpCustom;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepositoryDB;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class PlansManagerImpl implements PlansManager{
    private final PlansRepositoryDB dbRepository;
    private final PlansRepoHttpCustom httpRepository;

    @Transactional
    public Optional<Plans> findByName(String name) throws IOException, URISyntaxException, InterruptedException {
        // local db
        Optional<Plans> resultFromDB = dbRepository.findByName_Name(name);

        if (resultFromDB.isPresent()) {
            return resultFromDB;
        } else {
            // nao localemente
            Optional<Plans> resultFromHTTP = httpRepository.getPlanByNameNotLocally(name);
            return resultFromHTTP;
        }
    }

    @Override
    public Iterable<Plans> findByActive_Active(boolean b) {
        return dbRepository.findByActive_Active(b);
    }

    @Override
    public PlanRequest getAllExternal()throws URISyntaxException, IOException, InterruptedException {
        return httpRepository.getAllExternal();

    }

    @Override
    public Iterable<Plans> addPlanToIterable(Iterable<Plans> plans, Plans newPlan) {
        return  httpRepository.addPlanToIterable(plans,newPlan);
    }

    @Override
    public Plans save(Plans obj) {
        return dbRepository.save(obj);
    }

    @Override
    public Optional<Plans> findByNameName(String planName) {
        return dbRepository.findByName_Name(planName);
    }


}