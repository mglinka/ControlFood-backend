package pl.lodz.pl.it.mok.repository.allergy;

import pl.lodz.pl.it.entity.allergy.AllergyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface AllergyProfileRepository extends JpaRepository<AllergyProfile, UUID> {



}
