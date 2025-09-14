package com.simplesdental.product.model;

import com.simplesdental.product.DTOs.responses.ProductV2ResponseDTO;
import com.simplesdental.product.utils.ApiObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ProductV2Test {

    @Mock
    private ApiObjectMapper<ProductV2ResponseDTO> apiObjectMapper;

    private ProductV2 productV2;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1L);
        category.setName("Higiene Bucal");
        category.setDescription("Produtos para higiene diária dos dentes");

        productV2 = new ProductV2();
        productV2.setId(1L);
        productV2.setName("Escova Elétrica");
        productV2.setDescription("Escova elétrica recarregável com timer");
        productV2.setPrice(new BigDecimal("199.90"));
        productV2.setStatus(true);
        productV2.setCode(1001);
        productV2.setCategory(category);
    }

    @Test
    void testGetResponse() {
        ProductV2ResponseDTO dto = new ProductV2ResponseDTO(
                1L,
                "Escova Elétrica",
                "Escova elétrica recarregável com timer",
                new BigDecimal("199.90"),
                true,
                1001,
                category
        );

        when(apiObjectMapper.entityToDto(productV2, ProductV2ResponseDTO.class)).thenReturn(dto);

        ProductV2ResponseDTO response = productV2.getResponse(apiObjectMapper);

        assertEquals(1L, response.id());
        assertEquals("Escova Elétrica", response.name());
        assertEquals("Escova elétrica recarregável com timer", response.description());
        assertEquals(new BigDecimal("199.90"), response.price());
        assertEquals(true, response.status());
        assertEquals(1001, response.code());
        assertEquals("Higiene Bucal", response.category().getName());
    }
}
