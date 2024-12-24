package com.project.mopa.repository.allergy;

import com.project.mopa.entity.allergy.AllergyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AllergyProfileRepository extends JpaRepository<AllergyProfile, UUID> {



}
