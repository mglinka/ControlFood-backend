package com.project.mpa.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ProducerDTO {

    private String name;
    private String address;
    private int countryCode;
    private String contact;
    private String nip;
    private int rmsd;
}
