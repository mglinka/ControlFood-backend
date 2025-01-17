package pl.lodz.pl.it.mopa.entity;

import jakarta.persistence.Column;
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

    @Column(name = "groupname")
    private String groupName;
}
