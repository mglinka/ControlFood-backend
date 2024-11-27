package com.project.mpa.repository;

import com.project.mpa.entity.PackageType;
import com.project.mpa.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PackageTypeRepository extends JpaRepository<PackageType, UUID> {

    Optional<PackageType> findByName(String name);
}
