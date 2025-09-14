package com.simplesdental.product.DTOs.responses;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserResponseDTOTest {

    @Test
    void testUserResponseDTOCreation() {
        String name = "João da Silva";
        String email = "joao.silva@email.com";

        UserResponseDTO dto = new UserResponseDTO(name, email);

        assertEquals(name, dto.name());
        assertEquals(email, dto.email());
    }
}
