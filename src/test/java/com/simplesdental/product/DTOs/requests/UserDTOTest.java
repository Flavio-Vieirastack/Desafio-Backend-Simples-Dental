package com.simplesdental.product.DTOs.requests;

import com.simplesdental.product.Enums.Role;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUserDTO() {
        UserDTO dto = new UserDTO(
                "João Silva",
                "joao.silva@email.com",
                "senha123",
                Role.ADMIN
        );

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testNameBlank() {
        UserDTO dto = new UserDTO(
                "",
                "joao.silva@email.com",
                "senha123",
                Role.ADMIN
        );

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(dto);
        List<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        assertTrue(messages.contains("não deve estar em branco"));
    }

    @Test
    void testEmailInvalid() {
        UserDTO dto = new UserDTO(
                "João Silva",
                "email-invalido",
                "senha123",
                Role.ADMIN
        );

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(dto);
        List<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        assertTrue(messages.stream().anyMatch(m -> m.contains("deve ser um e-mail válido")));
    }

    @Test
    void testPasswordTooShortAndRoleNull() {
        UserDTO dto = new UserDTO(
                "João Silva",
                "joao.silva@email.com",
                "123",
                null
        );

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(dto);
        assertEquals(2, violations.size());

        List<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        assertTrue(messages.contains("deve ter no mínimo 6 caracteres"));
        assertTrue(messages.contains("não deve ser nulo"));
    }
}
