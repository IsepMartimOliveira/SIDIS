package com.example.psoft_22_23_project.devicemanagement.api;

import com.example.psoft_22_23_project.devicemanagement.model.Description;
import com.example.psoft_22_23_project.devicemanagement.model.Name;
import com.example.psoft_22_23_project.devicemanagement.model.MacAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditDeviceRequest {

    private String name;

    private String description;

}
