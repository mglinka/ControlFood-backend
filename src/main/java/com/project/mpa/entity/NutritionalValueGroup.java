package com.project.mpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "nutritional_value_group")
public class NutritionalValueGroup extends AbstractEntity {

    private String groupName;
}
