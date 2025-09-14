package com.simplesdental.product.DTOs.responses;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginResponseDTOTest {

    @Test
    void testLoginResponseDTOCreation() {
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
        Long expiresIn = 3600000L;
        String refreshToken = "d1f7e8c4-7f2a-4e1b-8d53-1a3f0c6c9b2e";

        LoginResponseDTO dto = new LoginResponseDTO(accessToken, expiresIn, refreshToken);

        assertEquals(accessToken, dto.accessToken());
        assertEquals(expiresIn, dto.expiresIn());
        assertEquals(refreshToken, dto.refreshToken());
    }
}
