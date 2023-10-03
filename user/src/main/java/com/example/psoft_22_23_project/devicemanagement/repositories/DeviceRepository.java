package com.example.psoft_22_23_project.devicemanagement.repositories;

import com.example.psoft_22_23_project.devicemanagement.model.Device;
import com.example.psoft_22_23_project.devicemanagement.model.MacAddress;
import com.example.psoft_22_23_project.devicemanagement.model.Name;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.usermanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    Optional<Device> findByMacAddress_MacAddress(String macAddress);
    void deleteById(Long id);

    @Modifying
    @Query("DELETE FROM Device f WHERE f.id = :deviceId AND f.version = :desiredVersion")
    int deleteByIdIfMatches(@Param("deviceId") Long deviceId, @Param("desiredVersion") long desiredVersion);
    int countBySubscription(Subscriptions subscriptions);
    List<Device> findAllBySubscription(Subscriptions subscription);
}
