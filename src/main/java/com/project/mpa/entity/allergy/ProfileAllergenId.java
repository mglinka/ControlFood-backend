package com.project.mpa.entity.allergy;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Data
@Getter
@Setter
@Embeddable
public class ProfileAllergenId implements Serializable {

    private UUID profile_id;
    private UUID allergen_id;
}
