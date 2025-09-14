package com.simplesdental.product.DTOs.responses;

import com.simplesdental.product.model.Category;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductV2ResponseDTOTest {

    @Test
    void testProductV2ResponseDTOCreation() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Higiene Bucal");
        category.setDescription("Produtos para higiene diária dos dentes");

        Long id = 1L;
        String name = "Escova de Dentes Elétrica";
        String description = "Escova de dentes elétrica recarregável com timer";
        BigDecimal price = new BigDecimal("199.90");
        Boolean status = true;
        Integer code = 1001;

        ProductV2ResponseDTO dto = new ProductV2ResponseDTO(id, name, description, price, status, code, category);

        assertEquals(id, dto.id());
        assertEquals(name, dto.name());
        assertEquals(description, dto.description());
        assertEquals(price, dto.price());
        assertEquals(status, dto.status());
        assertEquals(code, dto.code());
        assertEquals(category, dto.category());
    }
}
