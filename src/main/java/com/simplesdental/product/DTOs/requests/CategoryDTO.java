package com.simplesdental.product.DTOs.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryDTO(

        @NotBlank(message = "O nome da categoria é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        @Schema(description = "Nome da categoria", example = "Produtos de Higiene")
        String name,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
        @Schema(description = "Descrição detalhada da categoria", example = "Categoria que contém produtos de higiene pessoal")
        String description,

        @NotNull
        @Schema(description = "ID do produto associado à categoria", example = "1")
        Integer productId
) {}
