package com.project.mopa.entity;

import jakarta.persistence.Column;
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

    @Column(name = "portionquantity")
    private Integer portionQuantity;

    @ManyToOne
    private Unit unit;
}
