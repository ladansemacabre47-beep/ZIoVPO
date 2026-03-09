package com.example.lab.repository.license;

import com.example.lab.model.license.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
    Optional<Device> findByDeviceIdentifier(String deviceIdentifier);
}