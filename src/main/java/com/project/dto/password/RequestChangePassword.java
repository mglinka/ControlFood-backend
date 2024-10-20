package com.project.dto.password;

import lombok.Data;

import java.util.UUID;

@Data
public class RequestChangePassword {
    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;

}
