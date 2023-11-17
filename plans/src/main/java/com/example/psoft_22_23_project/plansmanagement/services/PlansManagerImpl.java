package com.example.psoft_22_23_project.plansmanagement.services;

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
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
    public Boolean findByNameDoesNotExists(String name) throws IOException, URISyntaxException, InterruptedException {

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
    public Optional<Plans> findByNameDoesExists(String name) throws IOException, URISyntaxException, InterruptedException {
        return Optional.empty();
    }

    @Override
    public Iterable<Plans> findByActive_Active(boolean b) {
        return dbRepository.findByActive_Active(b);
    }

    @Override
    public Iterable<Plans> addNotLocalPlans(Iterable<Plans> planslocal) throws URISyntaxException, IOException, InterruptedException {
        return httpRepository.addLocalPlusNot(planslocal);

    }

    @Override
    public Plans create(String auth, CreatePlanRequest resource) throws URISyntaxException, IOException, InterruptedException {
        return httpRepository.createNotLocal(auth,resource);
    }

    public Plans save(Plans obj) {
        return dbRepository.save(obj);
    }


}