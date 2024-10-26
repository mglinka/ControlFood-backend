package com.project.mok.repository;


import com.project.entity.Account;
import com.project.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
public interface AccountMokRepository extends JpaRepository<Account, UUID> {



    @Override
    Account saveAndFlush(Account account);


    @Override
    Optional<Account> findById(UUID id);


    Optional<Account> findByEmail(String email);

    List<Account> findAccountByRole(Role role);


    @Override
    void delete(Account account);

}