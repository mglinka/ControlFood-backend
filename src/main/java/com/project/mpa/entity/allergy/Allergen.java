package com.project.mpa.entity.allergy;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Allergen {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID allergen_id;

    @Column(nullable = false, unique = true)
    private String name;  // Nazwa alergenu


    @OneToMany(mappedBy = "allergen", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProfileAllergen> profileAllergens = new ArrayList<>();


}
