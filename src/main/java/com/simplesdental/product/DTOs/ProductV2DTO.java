package com.simplesdental.product.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductV2DTO(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull BigDecimal price,
        Boolean status,
        @NotNull Integer code,
        @NotNull Long categoryId
) {
}
