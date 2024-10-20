package com.project.mok.repository;


import com.project.entity.Account;
import com.project.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface AccountMokRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByUsername(String username);

    @PreAuthorize("permitAll()")
    @Override
    Account saveAndFlush(Account account);

    @PreAuthorize("permitAll()")
    @Override
    Optional<Account> findById(UUID id);

    @PreAuthorize("permitAll()")
    Optional<Account> findByEmail(String email);

    List<Account> findAccountByRolesContains(Role role);


    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    @Override
    void delete(Account account);

}