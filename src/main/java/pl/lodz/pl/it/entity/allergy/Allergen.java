package pl.lodz.pl.it.entity.allergy;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.*;

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Allergen {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID allergen_id;

    @Column(nullable = false, unique = true)
    private String name;

    @Version
    private Long version;

    @OneToMany(mappedBy = "allergen", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProfileAllergen> profileAllergens = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "type_enum")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private AllergenType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Allergen allergen = (Allergen) o;
        return Objects.equals(allergen_id, allergen.allergen_id) && Objects.equals(
            name, allergen.name) && Objects.equals(version, allergen.version)
            && type == allergen.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(allergen_id, name, version, type);
    }
}
