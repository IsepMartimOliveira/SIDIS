package com.example.loadbalancer.api;

import com.example.loadbalancer.model.Plans;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public  abstract class CreatePlanMapper {

    @Mapping(source = "request.name", target = "name")
    @Mapping(source = "request.description", target = "description")
    @Mapping(source = "request.numberOfMinutes", target = "numberOfMinutes")
    @Mapping(source = "request.maximumNumberOfUsers", target = "maximumNumberOfUsers")
    @Mapping(source = "request.musicCollection", target = "musicCollection")
    @Mapping(source = "request.musicSuggestion", target = "musicSuggestion")
    @Mapping(source = "request.annualFee", target = "annualFee")
    @Mapping(source = "request.monthlyFee", target = "monthlyFee")
    @Mapping(source = "request.active", target = "active")
    @Mapping(source = "request.promoted", target = "promoted")
    public abstract Plans create(CreatePlanRequest request);
    @Mapping(source = "request.name", target = "name")
    @Mapping(source = "request.description", target = "description")
    @Mapping(source = "request.numberOfMinutes", target = "numberOfMinutes")
    @Mapping(source = "request.maximumNumberOfUsers", target = "maximumNumberOfUsers")
    @Mapping(source = "request.musicCollection", target = "musicCollection")
    @Mapping(source = "request.musicSuggestion", target = "musicSuggestion")
    @Mapping(source = "request.annualFee", target = "annualFee")
    @Mapping(source = "request.monthlyFee", target = "monthlyFee")
    @Mapping(source = "request.active", target = "active")
    @Mapping(source = "request.promoted", target = "promoted")
    public abstract CreatePlanRequest createInverse(Plans request);

    @Mapping(source = "request.name", target = "name")
    @Mapping(source = "request.description", target = "description")
    @Mapping(source = "request.numberOfMinutes", target = "numberOfMinutes")
    @Mapping(source = "request.maximumNumberOfUsers", target = "maximumNumberOfUsers")
    @Mapping(source = "request.musicCollection", target = "musicCollection")
    @Mapping(source = "request.musicSuggestion", target = "musicSuggestion")
    @Mapping(source = "request.annualFee", target = "annualFee")
    @Mapping(source = "request.monthlyFee", target = "monthlyFee")
    @Mapping(source = "request.active", target = "active")
    @Mapping(source = "request.promoted", target = "promoted")
    public abstract Plans createBonus(CreatePlanRequestBonus request);

}
