package com.example.lab.repository.license;

import com.example.lab.model.license.LicenseType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LicenseTypeRepository extends JpaRepository<LicenseType, UUID> {
    Optional<LicenseType> findByName(String name);
}