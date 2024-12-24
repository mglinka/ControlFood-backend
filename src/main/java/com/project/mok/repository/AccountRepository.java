package com.project.mok.repository;

import com.project.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Override
    Account saveAndFlush(Account account);


    @Override
    Optional<Account> findById(UUID id);


    Optional<Account> findByEmail(String email);

    @Override
    void delete(Account account);

}
