package com.simplesdental.product.DTOs.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDTO(
        @NotBlank(message = "não deve estar vazio")
        @Email
        @Schema(
                description = "E-mail do usuário para login",
                example = "contato@simplesdental.com"
        )
        String email,

        @NotBlank(message = "não deve estar vazio")
        @Size(min = 6, message = "deve ter no mínimo 6 caracteres")
        @Schema(
                description = "Senha do usuário com mínimo de 6 caracteres",
                example = "KMbT%5wT*R!46i@@YHqx"
        )
        String password
) {
}
