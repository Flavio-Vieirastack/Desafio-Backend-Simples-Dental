package com.simplesdental.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplesdental.product.DTOs.requests.CategoryDTO;
import com.simplesdental.product.DTOs.responses.CategoryResponseDTO;
import com.simplesdental.product.model.Category;
import com.simplesdental.product.service.CategoryService;
import com.simplesdental.product.utils.ApiObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private ApiObjectMapper<CategoryResponseDTO> categoryResponseDTOApiObjectMapper;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void testGetAllCategories() throws Exception {
        Category category = new Category();
        CategoryResponseDTO responseDTO = new CategoryResponseDTO(1L, "Higiene Bucal", "Produtos para higiene diária dos dentes");

        when(categoryService.findAll(0, 10)).thenReturn(List.of(category));
        when(category.getResponse(categoryResponseDTOApiObjectMapper)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/categories")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Higiene Bucal"))
                .andExpect(jsonPath("$[0].description").value("Produtos para higiene diária dos dentes"));
    }

    @Test
    void testGetCategoryById() throws Exception {
        Category category = new Category();
        CategoryResponseDTO responseDTO = new CategoryResponseDTO(1L, "Higiene Bucal", "Produtos para higiene diária dos dentes");

        when(categoryService.findById(1L)).thenReturn(category);
        when(category.getResponse(categoryResponseDTOApiObjectMapper)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Higiene Bucal"))
                .andExpect(jsonPath("$.description").value("Produtos para higiene diária dos dentes"));
    }

    @Test
    void testCreateCategory() throws Exception {
        CategoryDTO dto = new CategoryDTO("Higiene Bucal", "Produtos para higiene diária dos dentes", 1);
        Category category = new Category();
        CategoryResponseDTO responseDTO = new CategoryResponseDTO(1L, dto.name(), dto.description());

        when(categoryService.save(any(CategoryDTO.class))).thenReturn(category);
        when(category.getResponse(categoryResponseDTOApiObjectMapper)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Higiene Bucal"))
                .andExpect(jsonPath("$.description").value("Produtos para higiene diária dos dentes"));
    }

    @Test
    void testUpdateCategory() throws Exception {
        CategoryDTO dto = new CategoryDTO("Higiene Bucal Atualizado", "Descrição atualizada", 1);
        Category category = new Category();
        CategoryResponseDTO responseDTO = new CategoryResponseDTO(1L, dto.name(), dto.description());

        when(categoryService.update(any(CategoryDTO.class), any(Long.class))).thenReturn(category);
        when(category.getResponse(categoryResponseDTOApiObjectMapper)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Higiene Bucal Atualizado"))
                .andExpect(jsonPath("$.description").value("Descrição atualizada"));
    }

    @Test
    void testDeleteCategory() throws Exception {
        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());
    }
}
