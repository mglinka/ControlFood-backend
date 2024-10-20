package com.project.mpa.entity;

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

//    CREATE TABLE public.product (
//            id uuid NOT NULL,
//            version bigint NOT NULL,
//            country text,
//            ean character varying(13) NOT NULL,
//             product_description character varying(255),
//             product_name character varying(255) NOT NULL,
//             product_quantity integer,
//             composition_id uuid,
//             label_id uuid,
//             packagetype_id uuid,
//             portion_id uuid,
//             producer_id uuid,
//             unit_id uuid
//      );


    @Size(max = 13)
    private String ean;

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

    @ManyToMany
    private Set<NutritionalIndex> nutritionalIndexes;

    @ManyToMany
    private Set<ProductIndex> productIndexes;

    @OneToOne
    private Label label;

    @OneToOne
    private Portion portion;

    @ManyToMany
    @JoinTable(
            name = "product_rating",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "ratings_id")
    )
    private Set<Rating> ratings;

    @ManyToMany
    private List<NutritionalValue> nutritionalValues;
}
