package com.simplesdental.product.DTOs.requests;

import com.simplesdental.product.Enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDTO(
        @NotBlank
        @Schema(description = "Nome completo do usuário", example = "João da Silva")
        String name,

        @NotBlank
        @Email
        @Schema(description = "Email do usuário, deve ser único e válido", example = "joao.silva@email.com")
        String email,

        @NotBlank
        @Min(6)
        @Schema(description = "Senha do usuário, mínimo 6 caracteres", example = "senha123")
        String password,

        @NotNull
        @Schema(description = "Função do usuário no sistema", example = "ADMIN")
        Role role
) {
}
