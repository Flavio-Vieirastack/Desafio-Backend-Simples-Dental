package com.simplesdental.product.DTOs.responses;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponseDTO(
        @Schema(description = "Nome completo do usuário", example = "João da Silva")
        String name,

        @Schema(description = "Email do usuário", example = "joao.silva@email.com")
        String email
) {
}
