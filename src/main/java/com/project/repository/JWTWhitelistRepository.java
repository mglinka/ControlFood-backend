package com.project.repository;

import com.project.entity.JWTWhitelistToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Transactional
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
public interface JWTWhitelistRepository extends JpaRepository<JWTWhitelistToken, UUID> {

    @PreAuthorize("permitAll()")
    Boolean existsByToken(String token);

    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    void deleteAllByExpirationDateBefore(LocalDateTime expirationDate);

    void deleteByToken(String token);

    @PreAuthorize("permitAll()")
    void deleteAllByAccount_Id(UUID accountId);

    @PreAuthorize("permitAll()")
    JWTWhitelistToken saveAndFlush(JWTWhitelistToken jwtWhitelistToken);

    Optional<JWTWhitelistToken> findByToken(String token);

}