package com.project.mpa.repository.allergy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AllergyProfileSchemaRepository extends JpaRepository<com.project.mpa.entity.allergy.AllergyProfileSchema, UUID> {
}
