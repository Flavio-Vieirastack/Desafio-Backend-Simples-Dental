package com.simplesdental.product.DTOs.requests;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidCategoryDTO() {
        CategoryDTO dto = new CategoryDTO("Produtos de Higiene", "Categoria que contém produtos de higiene pessoal", 1);

        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Não deve haver violações para DTO válido");
    }

    @Test
    void testInvalidCategoryNameBlank() {
        CategoryDTO dto = new CategoryDTO("", "Descrição válida", 1);

        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("O nome da categoria é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidCategoryNameTooLong() {
        String longName = "A".repeat(101);
        CategoryDTO dto = new CategoryDTO(longName, "Descrição válida", 1);

        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("O nome deve ter no máximo 100 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidDescriptionTooLong() {
        String longDesc = "A".repeat(256);
        CategoryDTO dto = new CategoryDTO("Nome válido", longDesc, 1);

        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("A descrição deve ter no máximo 255 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void testProductIdNull() {
        CategoryDTO dto = new CategoryDTO("Nome válido", "Descrição válida", null);

        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("não deve ser nulo", violations.iterator().next().getMessage());
    }
}
