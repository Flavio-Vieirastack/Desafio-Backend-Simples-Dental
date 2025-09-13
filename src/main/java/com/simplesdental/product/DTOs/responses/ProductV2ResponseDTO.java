package com.simplesdental.product.DTOs.responses;

import com.simplesdental.product.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "ProductV2Response", description = "DTO de resposta para produtos v2")
public record ProductV2ResponseDTO(

        @Schema(description = "ID do produto", example = "1")
        Long id,

        @Schema(description = "Nome do produto", example = "Escova de Dentes Elétrica")
        String name,

        @Schema(description = "Descrição do produto", example = "Escova de dentes elétrica recarregável com timer")
        String description,

        @Schema(description = "Preço do produto", example = "199.90")
        BigDecimal price,

        @Schema(description = "Status do produto", example = "true")
        Boolean status,

        @Schema(description = "Código do produto (inteiro)", example = "1001")
        Integer code,

        @Schema(description = "Categoria do produto")
        Category category

) {}
