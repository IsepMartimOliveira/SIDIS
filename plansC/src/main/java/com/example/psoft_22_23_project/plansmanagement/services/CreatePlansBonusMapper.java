package com.example.psoft_22_23_project.plansmanagement.services;

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequestBonus;
import com.example.psoft_22_23_project.plansmanagement.api.PlansBonus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public abstract class CreatePlansBonusMapper {

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
    public abstract PlansBonus createBonus(CreatePlanRequestBonus request);

}
