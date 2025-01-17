package pl.lodz.pl.it.entity.allergy;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import pl.lodz.pl.it.entity.allergy.Allergen;

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profile_allergen")
public class ProfileAllergen implements Serializable {

    @EmbeddedId
    private ProfileAllergenId id;

    @ManyToOne
    @MapsId("profileId")
    @JoinColumn(name = "profile_id", nullable = false)
    private AllergyProfile allergyProfile;

    @ManyToOne
    @MapsId("allergenId")
    @JoinColumn(name = "allergen_id", nullable = false)
    private Allergen allergen;

    @Column(name = "intensity", nullable = false)
    private String intensity;

    @Override
    public String toString() {
        return "ProfileAllergen{" +
                "intensity=" + intensity +
                ", allergen_id=" + (allergen != null ? allergen.getAllergen_id() : null) +
                '}';
    }


}
