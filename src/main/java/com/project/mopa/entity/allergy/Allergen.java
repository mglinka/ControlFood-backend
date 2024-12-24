package com.project.mopa.entity.allergy;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

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
    private String name;

    @Version
    private Long version;

    @OneToMany(mappedBy = "allergen", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProfileAllergen> profileAllergens = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "type_enum")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private AllergenType type;


}
