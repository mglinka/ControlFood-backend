package com.project.mpa.repository.allergy;

import com.project.mpa.entity.allergy.AllergyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AllergyProfileRepository extends JpaRepository<AllergyProfile, UUID> {



}
