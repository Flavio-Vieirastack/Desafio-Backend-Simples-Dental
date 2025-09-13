package com.simplesdental.product.DTOs;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductDTO(
        @NotBlank(message = "O nome do produto é obrigatório")
        @Size(max = 100, message = "O nome não pode ter mais de 100 caracteres")
        String name,

        @NotBlank(message = "A descrição do produto é obrigatória")
        @Size(max = 255, message = "A descrição não pode ter mais de 255 caracteres")
        String description,

        @NotNull(message = "O preço do produto é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que 0")
        BigDecimal price,

        @NotNull(message = "O status do produto é obrigatório")
        Boolean status,

        @Size(max = 50, message = "O código do produto não pode ter mais de 50 caracteres")
        String code,
        @NotNull
        Long categoryId
) {}
