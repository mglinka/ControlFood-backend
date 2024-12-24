package com.project.mopa.repository.allergy;

import com.project.mopa.entity.allergy.AllergyProfileSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AllergyProfileSchemaRepository extends JpaRepository<com.project.mopa.entity.allergy.AllergyProfileSchema, UUID> {

    Optional<AllergyProfileSchema> findByName(String name);

}
