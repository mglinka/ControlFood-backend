package com.project.mpa.repository.allergy;

import com.project.mpa.entity.allergy.ProfileAllergen;
import com.project.mpa.entity.allergy.ProfileAllergenId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileAllergenRepository extends JpaRepository<ProfileAllergen, ProfileAllergenId> {

    Optional<ProfileAllergen> findById(ProfileAllergenId id);



}
