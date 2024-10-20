package com.project.mpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;


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
