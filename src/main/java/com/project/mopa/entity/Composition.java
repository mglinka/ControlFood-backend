package com.project.mopa.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Composition extends AbstractEntity {

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "composition_ingredient",
            joinColumns = @JoinColumn(name = "composition_id")
    )
    private List<Ingredient> ingredients = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "composition_addition",
            joinColumns = @JoinColumn(name = "composition_id")
    )
    private List<Addition> additions = new ArrayList<>();

    @ManyToOne
    private Flavour flavour;

}
