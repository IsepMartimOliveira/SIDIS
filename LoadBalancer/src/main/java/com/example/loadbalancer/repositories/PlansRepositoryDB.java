
package com.example.loadbalancer.repositories;
import com.example.loadbalancer.model.Plans;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Configuration
public interface PlansRepositoryDB extends CrudRepository<Plans,Long> {
    List<Plans> findAll();


}



