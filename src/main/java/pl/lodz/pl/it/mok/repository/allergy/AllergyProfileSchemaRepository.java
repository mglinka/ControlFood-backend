package pl.lodz.pl.it.mok.repository.allergy;

import pl.lodz.pl.it.entity.allergy.AllergyProfileSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AllergyProfileSchemaRepository extends JpaRepository<AllergyProfileSchema, UUID> {

    Optional<AllergyProfileSchema> findByName(String name);

}
