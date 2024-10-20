package com.project.mpa.repository.allergy;

import com.project.mpa.entity.allergy.ProfileAllergen;
import com.project.mpa.entity.allergy.ProfileAllergenId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileAllergenRepository extends JpaRepository<ProfileAllergen, ProfileAllergenId> {

}
