package pl.lodz.pl.it.auth.repository;

import pl.lodz.pl.it.entity.AccountConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Transactional
public interface AccountConfirmationRepository extends JpaRepository<AccountConfirmation, UUID> {

    @PreAuthorize("permitAll()")
    Optional<AccountConfirmation> findByToken(String token);


    Optional<AccountConfirmation> findByAccount_Id(UUID id);

    @PreAuthorize("permitAll()")
    @Override
    AccountConfirmation saveAndFlush(AccountConfirmation accountConfirmation);

    @PreAuthorize("permitAll()")
    @Override
    void delete(AccountConfirmation accountConfirmation);

}