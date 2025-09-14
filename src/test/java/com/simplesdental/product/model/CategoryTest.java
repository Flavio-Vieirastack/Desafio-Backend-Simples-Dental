package com.simplesdental.product.model;

import com.simplesdental.product.DTOs.responses.CategoryResponseDTO;
import com.simplesdental.product.utils.ApiObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CategoryTest {

    @Mock
    private ApiObjectMapper<CategoryResponseDTO> apiObjectMapper;

    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1L);
        category.setName("Higiene Bucal");
        category.setDescription("Produtos para higiene diária dos dentes");
    }

    @Test
    void testGetResponse() {
        CategoryResponseDTO dto = new CategoryResponseDTO(1L, "Higiene Bucal", "Produtos para higiene diária dos dentes");
        when(apiObjectMapper.entityToDto(category, CategoryResponseDTO.class)).thenReturn(dto);

        CategoryResponseDTO response = category.getResponse(apiObjectMapper);

        assertEquals(1L, response.id());
        assertEquals("Higiene Bucal", response.name());
        assertEquals("Produtos para higiene diária dos dentes", response.description());
    }
}
