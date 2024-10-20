package com.project.entity;

import com.project.mpa.entity.allergy.AllergyProfile;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String username;

    @Version
    @Column(nullable = false)
    @EqualsAndHashCode.Exclude
    private Long version;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Role> roles = new ArrayList<>();
    private boolean isEnabled = false;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "allergy_profile_id", referencedColumnName = "profile_id")
    private AllergyProfile allergyProfile;

//    @Column(nullable = false)
//    @NotNull
//    private Boolean verified;
//
//    @Column(nullable = false)
//    @NotNull
//    private Boolean nonLocked;
//
//    @PastOrPresent
//    private LocalDateTime lastSuccessfulLogin;
//
//    @FutureOrPresent
//    private LocalDateTime lockedUntil;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private LanguageEnum language;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + firstName + '\'' +
                // Avoid printing the entire profile to prevent recursion
                ", allergyProfileId=" + (allergyProfile != null ? allergyProfile.getProfile_id() : null) +
                '}';
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().toString()))
                .toList();
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }
}
