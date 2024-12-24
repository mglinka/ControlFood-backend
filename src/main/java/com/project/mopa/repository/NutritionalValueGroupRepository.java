package com.project.mopa.repository;

import com.project.mopa.entity.NutritionalValueGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NutritionalValueGroupRepository extends JpaRepository<NutritionalValueGroup, UUID> {
    Optional<NutritionalValueGroup> findByGroupName(String groupName);
}
