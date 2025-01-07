package com.project.entity.allergy;

import com.project.entity.Account;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AllergyProfile{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID profile_id;

    @Version
    @Column(nullable = false)
    private Long version;

    @OneToOne
    private Account account;



    @OneToMany(mappedBy = "allergyProfile", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProfileAllergen> profileAllergens = new ArrayList<>();



    @Override
    public String toString() {
        return "AllergyProfile{" +
                "profile_id=" + profile_id +
                ", numberOfAllergens=" + (profileAllergens != null ? profileAllergens.size() : 0) +
                '}';
    }



}


