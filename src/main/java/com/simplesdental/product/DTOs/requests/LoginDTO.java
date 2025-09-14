package com.simplesdental.product.DTOs.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record LoginDTO(
        @NotEmpty
        @Email
        @Schema(
                description = "E-mail do usuário para login",
                example = "email1@meuemail.com"
        )
        String email,

        @NotEmpty
        @Size(min = 6)
        @Schema(
                description = "Senha do usuário com mínimo de 6 caracteres",
                example = "123456"
        )
        String password
) {
}
