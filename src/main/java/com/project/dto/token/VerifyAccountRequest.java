package com.project.dto.token;

import jakarta.validation.constraints.NotBlank;

public record VerifyAccountRequest(
        @NotBlank(message = "Token cannot be blank.")
        String token) {
}