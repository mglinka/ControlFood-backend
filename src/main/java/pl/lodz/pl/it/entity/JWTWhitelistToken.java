package pl.lodz.pl.it.entity;

import pl.lodz.pl.it.utils.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "jwt_whitelist_token")
@NoArgsConstructor
@Getter
public class JWTWhitelistToken extends AbstractEntity {

    @Column(unique = true, nullable = false, updatable = false)
    private String token;

    @Future
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public JWTWhitelistToken(String token, Date expirationDate, Account account) {
        this.token = token;
        this.expirationDate = expirationDate;
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JWTWhitelistToken that)) {
            return false;
        }

        return getToken().equals(that.getToken());
    }

    @Override
    public int hashCode() {
        return getToken().hashCode();
    }
}