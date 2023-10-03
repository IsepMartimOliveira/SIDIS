package com.example.psoft_22_23_project.devicemanagement.services;

import com.example.psoft_22_23_project.devicemanagement.api.CreateDeviceRequest;
import com.example.psoft_22_23_project.devicemanagement.model.Device;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public abstract class CreateDeviceMapper {

    @Mapping(source = "request.macAddress", target = "macAddress.macAddress")
    @Mapping(source = "request.name", target = "name.name")
    @Mapping(source = "request.description", target = "description.description")
    public abstract Device create(Subscriptions subscription, CreateDeviceRequest request);

}
