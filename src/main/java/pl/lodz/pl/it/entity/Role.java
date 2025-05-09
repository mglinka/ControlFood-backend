package pl.lodz.pl.it.entity;

import pl.lodz.pl.it.utils.AbstractEntity;
import pl.lodz.pl.it.utils._enum.AccountRoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role extends AbstractEntity implements GrantedAuthority {
    @Column(nullable = false, unique = true, updatable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private AccountRoleEnum name;

    public String getRoleName() {
        return name.name().replace("ROLE_", "");
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role role)) {
            return false;
        }
        return name != null && name.equals(role.getName());
    }

    @Override
    public final int hashCode() {
        if (name != null) {
            return name.hashCode();
        }
        return super.hashCode();
    }

    @Override
    public String getAuthority() {
        return name.toString();
    }
}
