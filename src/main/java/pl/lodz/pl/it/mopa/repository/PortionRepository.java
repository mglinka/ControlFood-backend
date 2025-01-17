package pl.lodz.pl.it.mopa.repository;

import pl.lodz.pl.it.mopa.entity.Portion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PortionRepository extends JpaRepository<Portion, UUID> {
}
