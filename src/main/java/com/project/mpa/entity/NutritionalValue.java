package com.project.mpa.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "nutritional_value")
public class NutritionalValue extends AbstractEntity {

    @ManyToOne
    private NutritionalValueName nutritionalValueName;

    private Double quantity;

    @ManyToOne
    private Unit unit;

    private Double nrv; // Referencja wartości spożycia - RWS

}
