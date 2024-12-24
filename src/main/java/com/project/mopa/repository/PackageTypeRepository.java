package com.project.mopa.repository;

import com.project.mopa.entity.PackageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PackageTypeRepository extends JpaRepository<PackageType, UUID> {

    Optional<PackageType> findByName(String name);
}
