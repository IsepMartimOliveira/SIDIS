package com.example.psoft_22_23_project.plansmanagement.services;

import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepositoryDB;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class PlansManagerImpl implements PlansManager{
    private final PlansRepositoryDB dbRepository;

    @Transactional
    public Optional<Plans> findByName(String name){

        Optional<Plans> resultFromDB = dbRepository.findByName_Name(name);

        return resultFromDB;

    }

    @Override
    public Iterable<Plans> findByActive_Active(boolean b) {
        return dbRepository.findByActive_Active(b);
    }


    @Override
    public Plans save(Plans obj) {
        return dbRepository.save(obj);
    }


    @Override
    public void findByNameDoesNotExists(String name) {
        Optional<Plans> resultFromDB = dbRepository.findByName_Name(name);
        if (resultFromDB.isPresent()) {
            throw new IllegalArgumentException("Plan with name " + name + " already exists!");

        }
    }


    public Optional<Plans> findByNameDoesExistsUpdate(String name) {
        Optional<Plans> resultFromDB = dbRepository.findByName_Name(name);
        if (resultFromDB.isPresent()) {
            return resultFromDB;
        }
        throw new IllegalArgumentException("Plan with name " + name + " doesn´t exists ");

    }

}