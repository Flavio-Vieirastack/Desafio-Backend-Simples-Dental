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

class ProductDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidProductDTO() {
        ProductDTO dto = new ProductDTO(
                "Escova de Dentes",
                "Escova de dentes macia para uso diário",
                new BigDecimal("12.50"),
                true,
                "PROD-001",
                1L
        );

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Não deve haver violações para DTO válido");
    }

    @Test
    void testNameBlank() {
        ProductDTO dto = new ProductDTO(
                "",
                "Descrição válida",
                new BigDecimal("12.50"),
                true,
                "PROD-001",
                1L
        );

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("O nome do produto é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void testDescriptionBlank() {
        ProductDTO dto = new ProductDTO(
                "Produto",
                "",
                new BigDecimal("12.50"),
                true,
                "PROD-001",
                1L
        );

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("A descrição do produto é obrigatória", violations.iterator().next().getMessage());
    }

    @Test
    void testPriceNullOrNegative() {
        ProductDTO dtoNull = new ProductDTO("Produto", "Descrição", null, true, "PROD-001", 1L);
        Set<ConstraintViolation<ProductDTO>> violationsNull = validator.validate(dtoNull);
        assertEquals(1, violationsNull.size());
        assertEquals("O preço do produto é obrigatório", violationsNull.iterator().next().getMessage());

        ProductDTO dtoNegative = new ProductDTO("Produto", "Descrição", new BigDecimal("0.0"), true, "PROD-001", 1L);
        Set<ConstraintViolation<ProductDTO>> violationsNegative = validator.validate(dtoNegative);
        assertEquals(1, violationsNegative.size());
        assertEquals("O preço deve ser maior que 0", violationsNegative.iterator().next().getMessage());
    }

    @Test
    void testStatusNull() {
        ProductDTO dto = new ProductDTO("Produto", "Descrição", new BigDecimal("12.50"), null, "PROD-001", 1L);
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("O status do produto é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void testCodeTooLong() {
        ProductDTO dto = new ProductDTO(
                "Produto",
                "Descrição",
                new BigDecimal("12.50"),
                true,
                "C".repeat(51),
                1L
        );

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("O código do produto não pode ter mais de 50 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void testCategoryIdNull() {
        ProductDTO dto = new ProductDTO(
                "Produto",
                "Descrição",
                new BigDecimal("12.50"),
                true,
                "PROD-001",
                null
        );

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }
}
