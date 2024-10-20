package com.project.mok.repository;

import com.project.entity.Account;
import com.project.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {


    Optional<Account> findByUsername(String username);

    @PreAuthorize("permitAll()")
    @Override
    Account saveAndFlush(Account account);

    @PreAuthorize("permitAll()")
    @Override
    Optional<Account> findById(UUID id);

    @PreAuthorize("permitAll()")
    Optional<Account> findByEmail(String email);

//    List<Account> findAccountByRolesContains(Role role);
//
//    @PreAuthorize("hasRole('ROLE_SYSTEM')")
//    List<Account> findByNonLockedFalseAndLockedUntilBefore(LocalDateTime dateTime);
//
//    @PreAuthorize("hasRole('ROLE_SYSTEM')")
//    List<Account> findByNonLockedTrueAndLastSuccessfulLoginBefore(LocalDateTime dateTime);

    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    @Override
    void delete(Account account);

}
