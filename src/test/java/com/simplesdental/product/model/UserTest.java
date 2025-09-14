package com.simplesdental.product.model;

import com.simplesdental.product.DTOs.responses.UserResponseDTO;
import com.simplesdental.product.Enums.Role;
import com.simplesdental.product.utils.ApiObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTest {

    private User user;

    @Mock
    private ApiObjectMapper<UserResponseDTO> apiObjectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("João da Silva");
        user.setEmail("joao.silva@email.com");
        user.setPassword("senha123");
        user.setRole(Role.ADMIN);
    }

    @Test
    void testGenerateClaims() {
        Long expiresIn = 3600L;
        JwtClaimsSet claims = user.generateClaims(expiresIn, Role.ADMIN);

        assertNotNull(claims);
        assertEquals("Simples Dental",  claims.getClaim("iss"));
        assertEquals("1", claims.getSubject());
        assertEquals(Role.ADMIN, claims.getClaim("scope"));
        assertTrue(claims.getExpiresAt().isAfter(claims.getIssuedAt()));
    }

    @Test
    void testGetResponse() {
        UserResponseDTO dto = new UserResponseDTO(user.getName(), user.getEmail());
        when(apiObjectMapper.entityToDto(user, UserResponseDTO.class)).thenReturn(dto);

        UserResponseDTO result = user.getResponse(apiObjectMapper);

        assertNotNull(result);
        assertEquals(user.getName(), result.name());
        assertEquals(user.getEmail(), result.email());
        verify(apiObjectMapper, times(1)).entityToDto(user, UserResponseDTO.class);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, user.getId());
        assertEquals("João da Silva", user.getName());
        assertEquals("joao.silva@email.com", user.getEmail());
        assertEquals("senha123", user.getPassword());
        assertEquals(Role.ADMIN, user.getRole());
    }
}
