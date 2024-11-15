package com.project.dto.account;

import com.project.entity.Role;
import lombok.Data;

import java.util.UUID;

@Data
public class GetAccountDTO {

    private UUID id;

    private String email;

    private String firstName;

    private String lastName;
}
