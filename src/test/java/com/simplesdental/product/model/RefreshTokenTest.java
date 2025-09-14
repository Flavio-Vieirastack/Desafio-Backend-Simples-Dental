package com.simplesdental.product.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenTest {

    private RefreshToken refreshToken;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("João da Silva");
        user.setEmail("joao.silva@email.com");
        user.setPassword("senha123");
        user.setRole(com.simplesdental.product.Enums.Role.USER);

        refreshToken = new RefreshToken();
        refreshToken.setId(1L);
        refreshToken.setUser(user);
        refreshToken.setToken("abc123");
    }

    @Test
    void testIsExpired_whenExpired_shouldReturnTrue() {
        Date pastDate = Date.from(Instant.now().minusSeconds(60));
        refreshToken.setExpiryDate(pastDate);

        assertTrue(refreshToken.isExpired());
    }

    @Test
    void testIsExpired_whenNotExpired_shouldReturnFalse() {
        Date futureDate = Date.from(Instant.now().plusSeconds(60));
        refreshToken.setExpiryDate(futureDate);

        assertFalse(refreshToken.isExpired());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, refreshToken.getId());
        assertEquals(user, refreshToken.getUser());
        assertEquals("abc123", refreshToken.getToken());
    }
}
