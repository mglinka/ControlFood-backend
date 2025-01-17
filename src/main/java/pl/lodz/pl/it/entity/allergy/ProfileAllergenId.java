package pl.lodz.pl.it.entity.allergy;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class ProfileAllergenId implements Serializable {

    private UUID profileId;
    private UUID allergenId;

    public ProfileAllergenId(UUID profileId, UUID allergenId) {
        this.profileId = profileId;
        this.allergenId = allergenId;
    }
}
