package com.simplesdental.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplesdental.product.DTOs.requests.ProductV2DTO;
import com.simplesdental.product.DTOs.responses.ProductV2ResponseDTO;
import com.simplesdental.product.model.Category;
import com.simplesdental.product.model.Product;
import com.simplesdental.product.model.ProductV2;
import com.simplesdental.product.service.ProductV2Service;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductV2ControllerTest {

    @Mock
    private ProductV2Service productService;

    @Mock
    private ApiObjectMapper<ProductV2ResponseDTO> apiObjectMapper;

    @InjectMocks
    private ProductV2Controller productV2Controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productV2Controller).build();
    }

    @Test
    void testGetAllProducts() throws Exception {
        ProductV2 product = mock(ProductV2.class);
        Category category = new Category();
        ProductV2ResponseDTO responseDTO = new ProductV2ResponseDTO(1L, "Escova Elétrica", "Escova elétrica recarregável", new BigDecimal("199.90"), true, 1001, category);

        when(productService.findAll(0, 10)).thenReturn(List.of(product));
        when(product.getResponse(apiObjectMapper)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v2/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Escova Elétrica"))
                .andExpect(jsonPath("$[0].price").value(199.90))
                .andExpect(jsonPath("$[0].code").value(1001));
    }

    @Test
    void testGetProductById() throws Exception {
        ProductV2 product = mock(ProductV2.class);
        Category category = new Category();
        ProductV2ResponseDTO responseDTO = new ProductV2ResponseDTO(1L, "Escova Elétrica", "Escova elétrica recarregável", new BigDecimal("199.90"), true, 1001, category);

        when(productService.findById(1L)).thenReturn(product);
        when(product.getResponse(apiObjectMapper)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v2/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Escova Elétrica"))
                .andExpect(jsonPath("$.price").value(199.90))
                .andExpect(jsonPath("$.code").value(1001));
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductV2DTO dto = new ProductV2DTO("Escova Elétrica", "Escova elétrica recarregável", new BigDecimal("199.90"), true, 1001, 1L);
        ProductV2 product = mock(ProductV2.class);
        Category category = new Category();
        ProductV2ResponseDTO responseDTO = new ProductV2ResponseDTO(1L, dto.name(), dto.description(), dto.price(), dto.status(), dto.code(), category);

        when(productService.save(any(ProductV2DTO.class))).thenReturn(product);
        when(product.getResponse(apiObjectMapper)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v2/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Escova Elétrica"))
                .andExpect(jsonPath("$.price").value(199.90))
                .andExpect(jsonPath("$.code").value(1001));
    }

    @Test
    void testUpdateProduct() throws Exception {
        ProductV2DTO dto = new ProductV2DTO("Escova Atualizada", "Descrição atualizada", new BigDecimal("210.00"), true, 1002, 1L);
        ProductV2 product = mock(ProductV2.class);
        Category category = new Category();
        ProductV2ResponseDTO responseDTO = new ProductV2ResponseDTO(1L, dto.name(), dto.description(), dto.price(), dto.status(), dto.code(), category);

        when(productService.update(any(ProductV2DTO.class), any(Long.class))).thenReturn(product);
        when(product.getResponse(apiObjectMapper)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v2/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Escova Atualizada"))
                .andExpect(jsonPath("$.price").value(210.00))
                .andExpect(jsonPath("$.code").value(1002));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/v2/products/1"))
                .andExpect(status().isNoContent());
    }
}
