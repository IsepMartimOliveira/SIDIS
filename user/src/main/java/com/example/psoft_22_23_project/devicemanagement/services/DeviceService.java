package com.example.psoft_22_23_project.devicemanagement.services;

import com.example.psoft_22_23_project.devicemanagement.api.CreateDeviceRequest;
import com.example.psoft_22_23_project.devicemanagement.api.EditDeviceRequest;
import com.example.psoft_22_23_project.devicemanagement.model.Device;
import com.example.psoft_22_23_project.devicemanagement.model.DeviceImage;

import java.util.List;

public interface DeviceService {

    List<Device> findAllDevicesByUser();

    Device  create(CreateDeviceRequest resource, DeviceImage imageResource);
    Device update(String macAddress, EditDeviceRequest resource, DeviceImage imageResource, long desiredVersion);

    int deleteDevice(String macAddress, final long desiredVersion);

}
