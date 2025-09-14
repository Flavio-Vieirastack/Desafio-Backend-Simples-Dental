package com.simplesdental.product.DTOs.responses;

import com.simplesdental.product.Enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserResponseContextDTOTest {

    @Test
    void testUserResponseContextDTOCreation() {
        String name = "João da Silva";
        String email = "joao.silva@email.com";
        Role role = Role.ADMIN;

        UserResponseContextDTO dto = new UserResponseContextDTO(name, email, role);

        assertEquals(name, dto.name());
        assertEquals(email, dto.email());
        assertEquals(role, dto.role());
    }
}
