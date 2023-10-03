package com.example.psoft_22_23_project.devicemanagement.repositories;

import com.example.psoft_22_23_project.devicemanagement.model.Device;
import com.example.psoft_22_23_project.devicemanagement.model.DeviceImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceImageRepository extends JpaRepository<DeviceImage, Long> {
}
