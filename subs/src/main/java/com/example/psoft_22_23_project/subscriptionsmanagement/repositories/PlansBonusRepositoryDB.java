package com.example.psoft_22_23_project.subscriptionsmanagement.repositories;

import com.example.psoft_22_23_project.subscriptionsmanagement.model.PlansDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlansBonusRepositoryDB extends CrudRepository<PlansDetails,Long>{
    Optional<PlansDetails> findByName(String name);
}


