package com.project.mpa.entity;

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

    @ManyToMany
    private List<Ingredient> ingredients = new ArrayList<>();

    @ManyToMany
    private List<Addition> additions = new ArrayList<>();

    @ManyToOne
    private Flavour flavour;

}
