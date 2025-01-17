package pl.lodz.pl.it.mopa.repository;

import pl.lodz.pl.it.mopa.entity.NutritionalIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NutritionalIndexRepository extends JpaRepository<NutritionalIndex, UUID> {
}
