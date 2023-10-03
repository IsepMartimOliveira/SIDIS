package com.example.psoft_22_23_project.devicemanagement.api;

import com.example.psoft_22_23_project.devicemanagement.model.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class DeviceViewMapper {

    @Mapping(source = "device.macAddress.macAddress", target = "macAddress")
    @Mapping(source = "device.name.name", target = "name")
    @Mapping(source = "device.description.description", target = "description")
    @Mapping(source = "deviceImage.fileName", target = "fileName")
    public abstract DeviceView toDeviceView(Device device);

    public abstract Iterable<DeviceView> toDevicesView(Iterable<Device> devices);

    public Integer mapOptInt(final Optional<Integer> i) {
        return i.orElse(null);
    }

    public Long mapOptLong(final Optional<Long> i) {
        return i.orElse(null);
    }

    public String mapOptString(final Optional<String> i) {
        return i.orElse(null);
    }

}
