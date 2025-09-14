package com.simplesdental.product.model;

import com.simplesdental.product.DTOs.responses.CategoryResponseDTO;
import com.simplesdental.product.DTOs.responses.ProductResponseDTO;
import com.simplesdental.product.utils.ApiObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ProductTest {

    @Mock
    private ApiObjectMapper<ProductResponseDTO> apiObjectMapper;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1L);
        category.setName("Higiene Bucal");
        category.setDescription("Produtos para higiene diária dos dentes");

        product = new Product();
        product.setId(1L);
        product.setName("Escova de Dentes");
        product.setDescription("Escova macia para uso diário");
        product.setPrice(new BigDecimal("12.50"));
        product.setStatus(true);
        product.setCode("PROD-001");
        product.setCategory(category);
    }

    @Test
    void testGetResponse() {
        ProductResponseDTO dto = new ProductResponseDTO(
                1L,
                "Escova de Dentes",
                "Escova macia para uso diário",
                new BigDecimal("12.50"),
                true,
                "PROD-001",
                new CategoryResponseDTO(1L, "Higiene Bucal", "Produtos para higiene diária dos dentes")
        );

        when(apiObjectMapper.entityToDto(product, ProductResponseDTO.class)).thenReturn(dto);

        ProductResponseDTO response = product.getResponse(apiObjectMapper);

        assertEquals(1L, response.id());
        assertEquals("Escova de Dentes", response.name());
        assertEquals("Escova macia para uso diário", response.description());
        assertEquals(new BigDecimal("12.50"), response.price());
        assertEquals(true, response.status());
        assertEquals("PROD-001", response.code());
        assertEquals("Higiene Bucal", response.category().name());
    }
}
