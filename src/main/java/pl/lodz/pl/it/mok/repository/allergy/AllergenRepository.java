package pl.lodz.pl.it.mok.repository.allergy;

import pl.lodz.pl.it.entity.allergy.Allergen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AllergenRepository extends JpaRepository<Allergen, UUID> {
}
