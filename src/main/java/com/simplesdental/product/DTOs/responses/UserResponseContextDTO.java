package com.simplesdental.product.DTOs.responses;

import com.simplesdental.product.Enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponseContextDTO(
        @Schema(description = "Nome completo do usuário", example = "João da Silva")
        String name,

        @Schema(description = "Email do usuário", example = "joao.silva@email.com")
        String email,

        @Schema(description = "Função do usuário no sistema", example = "ADMIN")
        Role role
) {
}
