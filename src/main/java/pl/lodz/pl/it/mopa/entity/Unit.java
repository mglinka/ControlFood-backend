package pl.lodz.pl.it.mopa.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Unit extends AbstractEntity {

    @NotNull
    private String name;

}
