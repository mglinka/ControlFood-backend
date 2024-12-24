package com.project.mopa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Producer extends AbstractEntity {

    @NotNull
    private String name;


    private String address;

    @Column(name = "country_code", nullable = false)
    private Integer countryCode;

    private String NIP;

    private Integer RMSD;

    private String contact;

}
