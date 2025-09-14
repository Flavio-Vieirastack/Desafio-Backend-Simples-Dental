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

class LoginDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidLoginDTO() {
        LoginDTO dto = new LoginDTO("email1@meuemail.com", "123456");

        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Não deve haver violações para DTO válido");
    }

    @Test
    void testEmptyEmail() {
        LoginDTO dto = new LoginDTO("", "123456");

        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("não deve estar vazio", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidEmail() {
        LoginDTO dto = new LoginDTO("invalid-email", "123456");

        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("deve ser um endereço de e-mail bem formado", violations.iterator().next().getMessage());
    }


    @Test
    void testPasswordTooShort() {
        LoginDTO dto = new LoginDTO("email1@meuemail.com", "123");

        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("deve ter no mínimo 6 caracteres", violations.iterator().next().getMessage());
    }
}
