package com.project.mopa.entity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Flavour extends AbstractEntity {

    @NotNull
    private String name;

}
