package com.project.mok.service;

import com.project.entity.Account;
import com.project.exception.abstract_exception.AppException;
import com.project.exception.mok.AccountNotFoundException;
import com.project.mok.repository.AccountMokRepository;
import com.project.utils.messages.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeService {

    private final AccountMokRepository accountMokRepository;

    private final PasswordEncoder passwordEncoder;


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SPECIALIST', 'ROLE_USER')")
    @Transactional
    public void getAccount() throws AppException {


    }
}