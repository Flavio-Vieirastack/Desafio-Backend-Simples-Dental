package com.simplesdental.product.DTOs.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ProductV2DTO(

        @NotBlank
        @Schema(description = "Nome do produto", example = "Escova de Dentes")
        String name,

        @NotBlank
        @Schema(description = "Descrição detalhada do produto", example = "Escova de dentes macia para uso diário")
        String description,

        @NotNull
        @Schema(description = "Preço do produto", example = "12.50")
        BigDecimal price,

        @Schema(description = "Indica se o produto está ativo", example = "true")
        Boolean status,

        @NotNull
        @Schema(description = "Código único do produto (inteiro na versão 2)", example = "1")
        Integer code,

        @NotNull
        @Schema(description = "ID da categoria associada ao produto", example = "1")
        Long categoryId
) {}
