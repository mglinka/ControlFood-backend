package com.project.mpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Label extends AbstractEntity {

    private String storage;

    private String durability;

    @Column(name = "instructions_after_opening")
    private String instructionsAfterOpening;

    private String preparation;

    private String allergens;

    //private byte[] image;

}
