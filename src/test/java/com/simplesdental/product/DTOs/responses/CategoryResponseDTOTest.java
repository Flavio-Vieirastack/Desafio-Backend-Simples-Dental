package com.simplesdental.product.DTOs.responses;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryResponseDTOTest {

    @Test
    void testCategoryResponseDTOCreation() {
        CategoryResponseDTO dto = new CategoryResponseDTO(
                1L,
                "Higiene Bucal",
                "Produtos para higiene diária dos dentes"
        );

        assertEquals(1L, dto.id());
        assertEquals("Higiene Bucal", dto.name());
        assertEquals("Produtos para higiene diária dos dentes", dto.description());
    }
}
