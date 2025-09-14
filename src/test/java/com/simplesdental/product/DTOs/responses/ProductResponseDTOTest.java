package com.simplesdental.product.DTOs.responses;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductResponseDTOTest {

    @Test
    void testProductResponseDTOCreation() {
        CategoryResponseDTO category = new CategoryResponseDTO(1L, "Higiene Bucal", "Produtos para higiene diária dos dentes");
        Long id = 1L;
        String name = "Escova de Dentes";
        String description = "Escova de dentes macia para uso diário";
        BigDecimal price = new BigDecimal("12.50");
        Boolean status = true;
        String code = "PROD-001";

        ProductResponseDTO dto = new ProductResponseDTO(id, name, description, price, status, code, category);

        assertEquals(id, dto.id());
        assertEquals(name, dto.name());
        assertEquals(description, dto.description());
        assertEquals(price, dto.price());
        assertEquals(status, dto.status());
        assertEquals(code, dto.code());
        assertEquals(category, dto.category());
    }
}
