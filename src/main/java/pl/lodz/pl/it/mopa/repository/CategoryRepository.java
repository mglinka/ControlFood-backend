package pl.lodz.pl.it.mopa.repository;

import pl.lodz.pl.it.mopa.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Override
    Optional<Category> findById(UUID uuid);

    Optional<Category> getCategoryByName(String name);

}
