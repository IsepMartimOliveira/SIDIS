package com.example.psoft_22_23_project.plansmanagement.services;

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import com.example.psoft_22_23_project.plansmanagement.api.EditPlansRequest;
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
    public void findByNameDoesNotExists(String name) throws IOException, URISyntaxException, InterruptedException {
        // local db
        Optional<Plans> resultFromDB = dbRepository.findByName_Name(name);
        if (resultFromDB.isPresent()) {
            throw new IllegalArgumentException("Plan with name " + name + " already exists locally!");
        } else {
            // nao localemente
            Optional<Plans> resultFromHTTP = httpRepository.getPlanByNameNotLocally(name);
            if (resultFromHTTP != null){
                throw new IllegalArgumentException("Plan with name " + name + " already exists not locally!");
            }
        }
    }

    @Override
    public Optional<Plans> findByNameDoesExistsUpdate(String name, final long desiredVersion, final EditPlansRequest resource, String auth) throws IOException, URISyntaxException, InterruptedException {
        Optional<Plans> resultFromDB = dbRepository.findByName_Name(name);
        if (resultFromDB.isPresent()) {
            return resultFromDB;
        } else {
            Optional<Plans> resultFromHTTP = httpRepository.getPlanByNameNotLocally(name);
            if (resultFromHTTP.isPresent()) {
                throw new IllegalArgumentException("Plan with name " + name + " exists in other machine!");
            } else {
                return Optional.empty();
            }
        }
    }



    @Override
    public Optional<Plans> findByNameDoesExists(String name, final long desiredVersion,String auth) throws IOException, URISyntaxException, InterruptedException {
        Optional<Plans> resultFromDB = dbRepository.findByName_Name(name);
        if (resultFromDB.isPresent()) {
            Plans plans1 = resultFromDB.get();

            if(!plans1.getActive().getActive()){
                throw new IllegalArgumentException("Plan with name " + name + " is already deactivate");
            }
            //des localemente
            return resultFromDB;
        } else {
            // nao localemente
            Optional<Plans> resultFromHTTP = httpRepository.getPlanByNameNotLocally(name);
            if (resultFromHTTP.isPresent()) {
                throw new IllegalArgumentException("Plan with name " + name + " exists in other machine!");
            } else {
                return Optional.empty();
            }
        }
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
    public Plans save(Plans obj) {
        return dbRepository.save(obj);
    }

    @Override
    public Optional<Plans> findByNameName(String planName) {
        return dbRepository.findByName_Name(planName);
    }


}