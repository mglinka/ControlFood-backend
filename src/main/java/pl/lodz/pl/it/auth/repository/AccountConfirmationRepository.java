package pl.lodz.pl.it.auth.repository;

import pl.lodz.pl.it.entity.AccountConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface AccountConfirmationRepository extends JpaRepository<AccountConfirmation, UUID> {

    @PreAuthorize("permitAll()")
    Optional<AccountConfirmation> findByToken(String token);

    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    List<AccountConfirmation> findByExpirationDateBefore(LocalDateTime now);

    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    Optional<AccountConfirmation> findByAccount_Id(UUID id);

    @PreAuthorize("permitAll()")
    @Override
    AccountConfirmation saveAndFlush(AccountConfirmation accountConfirmation);

    @PreAuthorize("permitAll()")
    @Override
    void delete(AccountConfirmation accountConfirmation);

}