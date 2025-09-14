package com.simplesdental.product.controller;

import com.simplesdental.product.DTOs.requests.LoginDTO;
import com.simplesdental.product.DTOs.requests.UserDTO;
import com.simplesdental.product.DTOs.responses.LoginResponseDTO;
import com.simplesdental.product.DTOs.responses.UserResponseContextDTO;
import com.simplesdental.product.DTOs.responses.UserResponseDTO;
import com.simplesdental.product.constants.AuthorityConstants;
import com.simplesdental.product.service.UserService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @Operation(
            summary = "Login do usuário",
            description = "Autentica o usuário e retorna tokens JWT",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso", content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
            }
    )
    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Parameter(description = "DTO contendo email e senha do usuário")
            @Valid @RequestBody LoginDTO loginDTO
    ) {
        return ResponseEntity.ok(userService.login(loginDTO));
    }

    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria um novo usuário no sistema",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos ou usuário já existe")
            }
    )
    @PostMapping(path = "/register")
    @PreAuthorize(AuthorityConstants.ADMIN)
    public ResponseEntity<UserResponseDTO> createNewUser(
            @Parameter(description = "DTO contendo informações do novo usuário")
            @Valid @RequestBody UserDTO userDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createNewUser(userDTO));
    }

    @Operation(
            summary = "Obter dados do usuário logado",
            description = "Retorna informações do usuário baseado no token JWT",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dados do usuário retornados com sucesso", content = @Content(schema = @Schema(implementation = UserResponseContextDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
            }
    )
    @GetMapping(path = "/context")
    public ResponseEntity<UserResponseContextDTO> getUserData(
            @Parameter(description = "Token JWT do usuário autenticado")
            JwtAuthenticationToken jwtAuthenticationToken
    ) {
        return ResponseEntity.ok(userService.getUserData(jwtAuthenticationToken));
    }

    @Operation(
            summary = "Alterar senha do usuário",
            description = "Altera a senha do usuário logado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "400", description = "Nova senha inválida")
            }
    )
    @PutMapping(path = "/users/password")
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "Nova senha do usuário") @RequestParam String newPassword,
            @Parameter(description = "Token JWT do usuário autenticado") JwtAuthenticationToken jwtAuthenticationToken
    ) {
        userService.changePassword(jwtAuthenticationToken, newPassword);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Atualizar token de acesso usando o refresh token",
            description = "Gera um novo token de acesso válido usando um refresh token existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Token atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Refresh token inválido ou mal formatado"
                    )
            }
    )
    @PostMapping(path = "/refresh-token")
    public ResponseEntity<LoginResponseDTO> refreshToken(
            @Parameter(description = "Refresh token válido para gerar um novo access token", required = true)
            @RequestParam String refreshToken
    ) {
        return ResponseEntity.ok(userService.refreshToken(refreshToken));
    }
}
