package pl.lodz.pl.it.mopa.repository;

import pl.lodz.pl.it.mopa.entity.NutritionalValueName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NutritionalValueNameRepository extends JpaRepository<NutritionalValueName, UUID> {


    Optional<NutritionalValueName> findByName(String name);
}
