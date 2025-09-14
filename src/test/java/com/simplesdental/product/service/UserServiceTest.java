package com.simplesdental.product.service;

import com.simplesdental.product.DTOs.requests.LoginDTO;
import com.simplesdental.product.DTOs.requests.UserDTO;
import com.simplesdental.product.DTOs.responses.LoginResponseDTO;
import com.simplesdental.product.DTOs.responses.UserResponseContextDTO;
import com.simplesdental.product.DTOs.responses.UserResponseDTO;
import com.simplesdental.product.Enums.Role;
import com.simplesdental.product.model.RefreshToken;
import com.simplesdental.product.model.User;
import com.simplesdental.product.repository.RefreshTokenRepository;
import com.simplesdental.product.repository.UserRepository;
import com.simplesdental.product.utils.ApiObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private ApiObjectMapper<User> apiObjectMapper;

    @Mock
    private ApiObjectMapper<UserResponseDTO> userResponseDTOApiObjectMapper;

    @Mock
    private ApiObjectMapper<UserResponseContextDTO> userResponseContextDTOApiObjectMapper;


    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setName("João");
        user.setEmail("joao@email.com");
        user.setPassword("encodedPass");
        user.setRole(Role.ADMIN);
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    }


    @Test
    void testIsPasswordCorrect() {
        LoginDTO loginDTO = new LoginDTO(user.getEmail(), "rawPassword");
        when(passwordEncoder.matches("rawPassword", "encodedPass")).thenReturn(true);

        assertTrue(userService.isPasswordCorrect(loginDTO, user.getPassword()));
    }

    @Test
    void testLoginSuccess() {
        LoginDTO loginDTO = new LoginDTO(user.getEmail(), "password");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArgument(0));

        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getTokenValue()).thenReturn("jwt-token");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

        LoginResponseDTO loginResponse = userService.login(loginDTO);

        assertNotNull(loginResponse);
        assertEquals("jwt-token", loginResponse.accessToken());
        assertNotNull(loginResponse.refreshToken());
    }



    @Test
    void testLoginUserNotFound() {
        LoginDTO loginDTO = new LoginDTO("notfound@email.com", "pass");
        when(userRepository.findByEmail(loginDTO.email())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.login(loginDTO));
    }

    @Test
    void testCreateRefreshToken() {
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArgument(0));

        String token = userService.createRefreshToken(user);

        assertNotNull(token);
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @Test
    void testGetUserIdFromToken() {
        JwtAuthenticationToken jwtAuthToken = mock(JwtAuthenticationToken.class);
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", "1");
        when(jwtAuthToken.getTokenAttributes()).thenReturn(claims);

        Long userId = userService.getUserIdFromToken(jwtAuthToken);
        assertEquals(1L, userId);
    }
}
