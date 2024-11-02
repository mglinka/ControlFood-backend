package com.project.mpa.entity.allergy;

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

    private UUID profile_id;
    private UUID allergen_id;
}
