package pl.lodz.pl.it.auth.repository;

import pl.lodz.pl.it.entity.Role;
import pl.lodz.pl.it.utils._enum.AccountRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Transactional
@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface RoleRepository extends JpaRepository<Role, UUID> {

    @PreAuthorize("permitAll()")
    Optional<Role> findByName(AccountRoleEnum role);
}
