package com.project.entity.token;

import com.project.entity.Account;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.time.Instant;

@DiscriminatorValue("ACCOUNT")
@Entity
@NoArgsConstructor
public class AccountVerificationToken extends VerificationToken {

    public static int EXPIRATION_TIME = 24 * 60;

    public AccountVerificationToken(String token, Instant expirationDate, Account account) {
        super(token, expirationDate, account);
    }
}
