package com.example.psoft_22_23_project.devicemanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "A Device")
public class DeviceView {

    // business identity
    @Schema(description = "The name of the Device")
    private String macAddress;

    private String name;

    private String description;

    private String fileName;

}
