package pl.lodz.pl.it.mopa.repository;

import pl.lodz.pl.it.mopa.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LabelRepository extends JpaRepository<Label, UUID> {
}
