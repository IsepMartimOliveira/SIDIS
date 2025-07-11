package com.example.psoft_22_23_project.plansmanagement.api;

import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public abstract class EditPlansUpdate {

    public abstract EditPlanRequestUpdate toEditRequest(String name,EditPlansRequest editPlansRequest,long desiredVersion);
}
