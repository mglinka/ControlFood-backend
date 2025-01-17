package pl.lodz.pl.it.mopa.entity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Ingredient extends AbstractEntity {

    @NotNull
    private String name;

}
