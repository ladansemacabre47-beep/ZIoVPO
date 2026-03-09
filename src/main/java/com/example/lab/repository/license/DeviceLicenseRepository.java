package com.example.lab.repository.license;

import com.example.lab.model.license.Device;
import com.example.lab.model.license.DeviceLicense;
import com.example.lab.model.license.License;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceLicenseRepository extends JpaRepository<DeviceLicense, UUID> {

    long countByLicense(License license);

    Optional<DeviceLicense> findByLicenseAndDevice(License license, Device device);

    List<DeviceLicense> findByLicense(License license);
}