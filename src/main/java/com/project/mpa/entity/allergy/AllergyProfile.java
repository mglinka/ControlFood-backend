package com.project.mpa.entity.allergy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.entity.Account;
import com.project.mpa.dto.GetAllergenDTO;
import com.project.mpa.entity.AbstractEntity;
import com.project.mpa.entity.allergy.Allergen;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

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


