package com.project.mpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "nutritional_index")
public class NutritionalIndex extends AbstractEntity {

    @Column(name = "index_value")
    private Integer indexValue;

    private String legend;
    // Assuming NutritionalIndex is related to Product
    @ManyToMany(mappedBy = "nutritionalIndexes")

    private Set<Product> products;


    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NutritionalIndex that)) {
            return false;
        }

        return Objects.equals(getIndexValue(), that.getIndexValue()) && Objects.equals(getLegend(), that.getLegend());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getIndexValue());
        result = 31 * result + Objects.hashCode(getLegend());
        return result;
    }
}
