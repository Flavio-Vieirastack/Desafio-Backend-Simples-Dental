package com.simplesdental.product.DTOs.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ProductResponseDTO(

        @Schema(description = "ID do produto", example = "1")
        Long id,

        @Schema(description = "Nome do produto", example = "Escova de Dentes")
        String name,

        @Schema(description = "Descrição detalhada do produto", example = "Escova de dentes macia para uso diário")
        String description,

        @Schema(description = "Preço do produto", example = "12.50")
        BigDecimal price,

        @Schema(description = "Status do produto (ativo/inativo)", example = "true")
        Boolean status,

        @Schema(description = "Código único do produto", example = "PROD-001")
        String code,

        @Schema(description = "Categoria associada ao produto")
        CategoryResponseDTO category
) {}
