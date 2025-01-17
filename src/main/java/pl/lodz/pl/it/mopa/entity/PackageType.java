package pl.lodz.pl.it.mopa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "package_type")
public class PackageType extends AbstractEntity {

    @Column(name = "name")
    private String name;

}

//CREATE TABLE public.package_type (
//        id uuid NOT NULL,
//        version bigint NOT NULL,
//        name character varying(255)
//);

