package com.project.mpa.repository;

import com.project.mpa.entity.Unit;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UnitRepository extends JpaRepository<Unit, UUID> {
    Optional<Unit> findByName(String name);
}
