package com.simplesdental.product.controller;

import com.simplesdental.product.DTOs.requests.LoginDTO;
import com.simplesdental.product.DTOs.requests.UserDTO;
import com.simplesdental.product.DTOs.responses.LoginResponseDTO;
import com.simplesdental.product.DTOs.responses.UserResponseContextDTO;
import com.simplesdental.product.DTOs.responses.UserResponseDTO;
import com.simplesdental.product.Enums.Role;
import com.simplesdental.product.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testLogin() throws Exception {
        LoginDTO loginDTO = new LoginDTO("test@example.com", "123456");

        LoginResponseDTO responseDTO = new LoginResponseDTO("fakeAccessToken", 900L, "fakeRefreshToken");

        when(userService.login(any(LoginDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("fakeAccessToken"))
                .andExpect(jsonPath("$.refreshToken").value("fakeRefreshToken"));
    }

    @Test
    void testCreateNewUser() throws Exception {
        UserDTO userDTO = new UserDTO("jhon doe","newuser@example.com", "123456", Role.ADMIN);
        UserResponseDTO responseDTO = new UserResponseDTO("jhon doe","newuser@example.com");

        when(userService.createNewUser(any(UserDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("newuser@example.com"));
    }

    @Test
    void testGetUserData() throws Exception {
        UserResponseContextDTO responseDTO = new UserResponseContextDTO("jhon doe", "user@example.com", Role.ADMIN);

        JwtAuthenticationToken token = null;
        when(userService.getUserData(any())).thenReturn(responseDTO);

        mockMvc.perform(get("/auth/context"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    void testChangePassword() throws Exception {
        JwtAuthenticationToken token = null;

        mockMvc.perform(put("/auth/users/password")
                        .param("newPassword", "new123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testRefreshToken() throws Exception {
        LoginResponseDTO responseDTO = new LoginResponseDTO("newAccessToken", 900L, "newRefreshToken");

        when(userService.refreshToken("refresh123")).thenReturn(responseDTO);

        mockMvc.perform(post("/auth/refresh-token")
                        .param("refreshToken", "refresh123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("newAccessToken"))
                .andExpect(jsonPath("$.refreshToken").value("newRefreshToken"));
    }
}
