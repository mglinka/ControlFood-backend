package com.project.dto.account;

import com.project.entity.Role;
import com.project.utils._enum.AccountRoleEnum;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class GetAccountPersonalDTO {

    UUID id;

    String username;

    String email;

    Role role;

    Boolean active;

    Boolean verified;

    Boolean nonLocked;

    String firstName;

    String lastName;


    Integer gender;
}
