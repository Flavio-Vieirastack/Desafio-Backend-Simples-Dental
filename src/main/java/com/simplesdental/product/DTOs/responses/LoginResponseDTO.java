package com.simplesdental.product.DTOs.responses;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDTO(
        @Schema(description = "Token de acesso JWT para autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,

        @Schema(description = "Tempo de expiração do token em milissegundos", example = "3600000")
        Long expiresIn,

        @Schema(description = "Token de atualização usado para obter um novo token de acesso", example = "d1f7e8c4-7f2a-4e1b-8d53-1a3f0c6c9b2e")
        String refreshToken
) {
}
