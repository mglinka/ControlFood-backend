package com.project.entity.allergy;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "allergy_profile_schema") // Optional: Specifies the table name in the database
public class AllergyProfileSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "schema_id", updatable = false, nullable = false)
    private UUID schema_id;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(nullable = false) // Optional: Ensures the name cannot be null
    private String name;

    @ManyToMany(fetch = FetchType.EAGER) // Assuming an AllergyProfileSchema can have multiple allergens and vice versa
    @JoinTable(
            name = "allergy_profile_schema_allergen", // Join table name
            joinColumns = @JoinColumn(name = "schema_id"), // FK in join table referencing AllergyProfileSchema
            inverseJoinColumns = @JoinColumn(name = "allergen_id") // FK in join table referencing Allergen
    )
    private List<Allergen> allergens = new ArrayList<>();
}
