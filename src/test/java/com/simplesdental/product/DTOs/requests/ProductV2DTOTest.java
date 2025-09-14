package com.simplesdental.product.DTOs.requests;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductV2DTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidProductV2DTO() {
        ProductV2DTO dto = new ProductV2DTO(
                "Escova de Dentes",
                "Escova de dentes macia para uso diário",
                new BigDecimal("12.50"),
                true,
                1,
                1L
        );

        Set<ConstraintViolation<ProductV2DTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Não deve haver violações para DTO válido");
    }

    @Test
    void testNameBlank() {
        ProductV2DTO dto = new ProductV2DTO(
                "",
                "Descrição válida",
                new BigDecimal("12.50"),
                true,
                1,
                1L
        );

        Set<ConstraintViolation<ProductV2DTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("não deve estar em branco", violations.iterator().next().getMessage());
    }

    @Test
    void testDescriptionBlank() {
        ProductV2DTO dto = new ProductV2DTO(
                "Produto",
                "",
                new BigDecimal("12.50"),
                true,
                1,
                1L
        );

        Set<ConstraintViolation<ProductV2DTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("não deve estar em branco", violations.iterator().next().getMessage());
    }

    @Test
    void testPriceNull() {
        ProductV2DTO dto = new ProductV2DTO(
                "Produto",
                "Descrição",
                null,
                true,
                1,
                1L
        );

        Set<ConstraintViolation<ProductV2DTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("não deve ser nulo", violations.iterator().next().getMessage());
    }

    @Test
    void testCodeNull() {
        ProductV2DTO dto = new ProductV2DTO(
                "Produto",
                "Descrição",
                new BigDecimal("12.50"),
                true,
                null,
                1L
        );

        Set<ConstraintViolation<ProductV2DTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("não deve ser nulo", violations.iterator().next().getMessage());
    }

    @Test
    void testCategoryIdNull() {
        ProductV2DTO dto = new ProductV2DTO(
                "Produto",
                "Descrição",
                new BigDecimal("12.50"),
                true,
                1,
                null
        );

        Set<ConstraintViolation<ProductV2DTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("não deve ser nulo", violations.iterator().next().getMessage());
    }
}
