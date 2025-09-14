package com.simplesdental.product.DTOs.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record LoginDTO(
        @NotEmpty
        @Email
        @Schema(example = "email1@meuemail.com")
        String email,
        @NotEmpty
        @Schema(example = "123456")
        @Size(min = 6)
        String password
) {
}
