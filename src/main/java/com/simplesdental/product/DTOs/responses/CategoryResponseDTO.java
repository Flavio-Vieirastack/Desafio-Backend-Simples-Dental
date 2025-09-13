package com.simplesdental.product.DTOs.responses;

import io.swagger.v3.oas.annotations.media.Schema;


public record CategoryResponseDTO(

        @Schema(description = "ID da categoria", example = "1")
        Long id,

        @Schema(description = "Nome da categoria", example = "Higiene Bucal")
        String name,

        @Schema(description = "Descrição detalhada da categoria", example = "Produtos para higiene diária dos dentes")
        String description
) {}
