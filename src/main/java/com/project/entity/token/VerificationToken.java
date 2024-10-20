package com.project.entity.token;

import com.project.entity.Account;
import com.project.utils.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Table(name = "tokens",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"account_id", "purpose"})
        },
        indexes = {
                @Index(name = "idx_token_account_id", columnList = "account_id")
        }

)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "purpose")
public abstract class VerificationToken extends AbstractEntity implements Serializable {

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Account account;
}
