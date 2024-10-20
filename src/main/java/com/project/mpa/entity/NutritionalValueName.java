package com.project.mpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "nutritional_value_name")
public class NutritionalValueName extends AbstractEntity {

    @ManyToOne
    private NutritionalValueGroup group;

    private String name;
}
