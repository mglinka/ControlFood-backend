package com.project.mopa.repository.allergy;

import com.project.mopa.entity.allergy.ProfileAllergen;
import com.project.mopa.entity.allergy.ProfileAllergenId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileAllergenRepository extends JpaRepository<ProfileAllergen, ProfileAllergenId> {

    Optional<ProfileAllergen> findById(ProfileAllergenId id);


    List<ProfileAllergen> findById_ProfileId(UUID profileId);

    @Modifying
    @Query("DELETE FROM ProfileAllergen pa WHERE pa.id.profileId = :profileId AND pa.id.allergenId = :allergenId")
    void deleteProfileAllergen(@Param("profileId") UUID profileId, @Param("allergenId") UUID allergenId);


}
