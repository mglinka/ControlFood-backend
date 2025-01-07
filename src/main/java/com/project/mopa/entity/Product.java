package com.project.mopa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;


import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Product extends AbstractEntity {

    @Size(max = 13)
    private String ean;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    private Producer producer;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "product_quantity")
    private Integer productQuantity;

    @ManyToOne
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "packagetype_id")
    private PackageType packageType;

    @Column(name = "country", length = Integer.MAX_VALUE)
    private String country;

    @OneToOne
    private Composition composition;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_nutritional_index",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "nutritionalindexes_id")
    )
    private Set<NutritionalIndex> nutritionalIndexes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_product_index",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "productindexes_id")
    )

    private Set<ProductIndex> productIndexes;

    @OneToOne
    private Label label;

    @OneToOne
    private Portion portion;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_rating",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "ratings_id")
    )
    private Set<Rating> ratings;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_nutritional_value",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "nutritionalvalues_id")
    )
    private List<NutritionalValue> nutritionalValues;
}
