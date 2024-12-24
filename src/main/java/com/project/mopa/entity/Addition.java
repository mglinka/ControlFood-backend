package com.project.mopa.entity;

import jakarta.persistence.Entity;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Addition extends AbstractEntity {

    private String name;

}
