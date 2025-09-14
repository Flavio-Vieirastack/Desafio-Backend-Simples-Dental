package com.simplesdental.product.DTOs.requests;

import com.simplesdental.product.Enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record UserDTO(
        @NotBlank
        @Schema(description = "Nome completo do usuário", example = "João da Silva")
        String name,

        @NotBlank
        @Email(message = "deve ser um e-mail válido")
        @Schema(description = "Email do usuário, deve ser único e válido", example = "joao.silva@email.com")
        String email,

        @NotBlank
        @Schema(description = "Senha do usuário, mínimo 6 caracteres", example = "senha123")
        @Size(min = 6, message = "deve ter no mínimo 6 caracteres")
        String password,

        @NotNull
        @Schema(description = "Função do usuário no sistema", example = "ADMIN")
        Role role
) {
}
