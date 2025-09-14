package com.simplesdental.product.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplesdental.product.model.Category;
import com.simplesdental.product.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JsonTest
class JacksonConfigTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerializeLazyEntities() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(new java.math.BigDecimal("10.00"));
        product.setCategory(category);

        assertDoesNotThrow(() -> objectMapper.writeValueAsString(product));
    }
}
