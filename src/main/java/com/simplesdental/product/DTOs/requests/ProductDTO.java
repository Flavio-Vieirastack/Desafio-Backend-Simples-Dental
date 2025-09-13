package com.simplesdental.product.DTOs.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ProductDTO(

        @NotBlank(message = "O nome do produto é obrigatório")
        @Size(max = 100, message = "O nome não pode ter mais de 100 caracteres")
        @Schema(description = "Nome do produto", example = "Escova de Dentes")
        String name,

        @NotBlank(message = "A descrição do produto é obrigatória")
        @Size(max = 255, message = "A descrição não pode ter mais de 255 caracteres")
        @Schema(description = "Descrição detalhada do produto", example = "Escova de dentes macia para uso diário")
        String description,

        @NotNull(message = "O preço do produto é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que 0")
        @Schema(description = "Preço do produto", example = "12.50")
        BigDecimal price,

        @NotNull(message = "O status do produto é obrigatório")
        @Schema(description = "Indica se o produto está ativo", example = "true")
        Boolean status,

        @Size(max = 50, message = "O código do produto não pode ter mais de 50 caracteres")
        @Schema(description = "Código único do produto", example = "PROD-001")
        String code,

        @NotNull
        @Schema(description = "ID da categoria associada ao produto", example = "1")
        Long categoryId
) {}
