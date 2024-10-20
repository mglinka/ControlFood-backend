package com.project.mpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Portion extends AbstractEntity {

    private Integer portionQuantity;

    @ManyToOne
    private Unit unit;
}
