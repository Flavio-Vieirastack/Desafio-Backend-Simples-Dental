package com.simplesdental.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplesdental.product.DTOs.requests.ProductDTO;
import com.simplesdental.product.DTOs.responses.CategoryResponseDTO;
import com.simplesdental.product.DTOs.responses.ProductResponseDTO;
import com.simplesdental.product.model.Product;
import com.simplesdental.product.service.ProductService;
import com.simplesdental.product.utils.ApiObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ApiObjectMapper<ProductResponseDTO> productResponseDTOApiObjectMapper;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testGetAllProducts() throws Exception {
        Product product = new Product();
        CategoryResponseDTO categoryDTO = new CategoryResponseDTO(1L, "Higiene Bucal", "Produtos para higiene diária dos dentes");
        ProductResponseDTO responseDTO = new ProductResponseDTO(1L, "Escova de Dentes", "Escova macia para uso diário", new BigDecimal("12.50"), true, "PROD-001", categoryDTO);

        when(productService.findAll(0, 10)).thenReturn(List.of(product));
        when(product.getResponse(productResponseDTOApiObjectMapper)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Escova de Dentes"))
                .andExpect(jsonPath("$[0].description").value("Escova macia para uso diário"))
                .andExpect(jsonPath("$[0].price").value(12.50))
                .andExpect(jsonPath("$[0].status").value(true))
                .andExpect(jsonPath("$[0].code").value("PROD-001"))
                .andExpect(jsonPath("$[0].category.id").value(1L));
    }

    @Test
    void testGetProductById() throws Exception {
        Product product = new Product();
        CategoryResponseDTO categoryDTO = new CategoryResponseDTO(1L, "Higiene Bucal", "Produtos para higiene diária dos dentes");
        ProductResponseDTO responseDTO = new ProductResponseDTO(1L, "Escova de Dentes", "Escova macia para uso diário", new BigDecimal("12.50"), true, "PROD-001", categoryDTO);

        when(productService.findById(1L)).thenReturn(product);
        when(product.getResponse(productResponseDTOApiObjectMapper)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Escova de Dentes"))
                .andExpect(jsonPath("$.category.id").value(1L));
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductDTO dto = new ProductDTO("Escova de Dentes", "Escova macia para uso diário", new BigDecimal("12.50"), true, "PROD-001", 1L);
        Product product = new Product();
        CategoryResponseDTO categoryDTO = new CategoryResponseDTO(1L, "Higiene Bucal", "Produtos para higiene diária dos dentes");
        ProductResponseDTO responseDTO = new ProductResponseDTO(1L, dto.name(), dto.description(), dto.price(), dto.status(), dto.code(), categoryDTO);

        when(productService.save(any(ProductDTO.class))).thenReturn(product);
        when(product.getResponse(productResponseDTOApiObjectMapper)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Escova de Dentes"))
                .andExpect(jsonPath("$.price").value(12.50))
                .andExpect(jsonPath("$.category.id").value(1L));
    }

    @Test
    void testUpdateProduct() throws Exception {
        ProductDTO dto = new ProductDTO("Escova Atualizada", "Descrição atualizada", new BigDecimal("15.00"), true, "PROD-002", 1L);
        Product product = new Product();
        CategoryResponseDTO categoryDTO = new CategoryResponseDTO(1L, "Higiene Bucal", "Produtos para higiene diária dos dentes");
        ProductResponseDTO responseDTO = new ProductResponseDTO(1L, dto.name(), dto.description(), dto.price(), dto.status(), dto.code(), categoryDTO);

        when(productService.updateProduct(any(ProductDTO.class), any(Long.class))).thenReturn(product);
        when(product.getResponse(productResponseDTOApiObjectMapper)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Escova Atualizada"))
                .andExpect(jsonPath("$.price").value(15.00))
                .andExpect(jsonPath("$.code").value("PROD-002"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }
}
