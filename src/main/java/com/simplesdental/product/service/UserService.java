package com.simplesdental.product.service;

import com.simplesdental.product.DTOs.requests.LoginDTO;
import com.simplesdental.product.DTOs.requests.UserDTO;
import com.simplesdental.product.DTOs.responses.LoginResponseDTO;
import com.simplesdental.product.DTOs.responses.UserResponseContextDTO;
import com.simplesdental.product.DTOs.responses.UserResponseDTO;
import com.simplesdental.product.annotations.TransactionalReadOnly;
import com.simplesdental.product.model.RefreshToken;
import com.simplesdental.product.model.User;
import com.simplesdental.product.repository.RefreshTokenRepository;
import com.simplesdental.product.repository.UserRepository;
import com.simplesdental.product.utils.ApiObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ApiObjectMapper<User> apiObjectMapper;
    private final ApiObjectMapper<UserResponseDTO> userResponseDTOApiObjectMapper;
    private final ApiObjectMapper<UserResponseContextDTO> userResponseContextDTOApiObjectMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Long tokenDuration = 1500L;
    private final Long refreshTokenDuration = tokenDuration * 3;

    @Transactional
    public LoginResponseDTO login(LoginDTO loginDTO) {
        log.info("Tentativa de login para o email: {}", loginDTO.email());
        var user = userRepository.findByEmail(loginDTO.email())
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado: {}", loginDTO.email());
                    return new EntityNotFoundException("Usuário não encontrado");
                });

        if (!isPasswordCorrect(loginDTO, user.getPassword())) {
            log.warn("Senha incorreta para o usuário: {}", loginDTO.email());
            throw new BadCredentialsException("Dados inválido");
        }

        var token = jwtEncoder.encode(
                JwtEncoderParameters.from(user.generateClaims(tokenDuration, user.getRole()))
        ).getTokenValue();

        String refreshToken = createRefreshToken(user);
        log.info("Login bem-sucedido para o usuário: {}", loginDTO.email());
        return new LoginResponseDTO(token, tokenDuration, refreshToken);
    }

    @TransactionalReadOnly
    public LoginResponseDTO refreshToken(String refreshToken) {
        log.info("Solicitação de refresh token: {}", refreshToken);
        var expiresIn = refreshTokenDuration;
        var storedToken = refreshTokenRepository.findByToken(refreshToken);

        if (storedToken.isEmpty() || storedToken.get().isExpired()) {
            log.warn("Refresh token inválido ou expirado: {}", refreshToken);
            throw new BadCredentialsException("Token inválido ou expirado");
        }

        User user = storedToken.get().getUser();
        var token = jwtEncoder.encode(
                JwtEncoderParameters.from(user.generateClaims(expiresIn, user.getRole()))
        ).getTokenValue();
        log.info("Refresh token emitido com sucesso para o usuário: {}", user.getEmail());
        return new LoginResponseDTO(token, expiresIn, refreshToken);
    }

    @Transactional
    public UserResponseDTO createNewUser(UserDTO userDTO) {
        log.info("Criando novo usuário com email: {}", userDTO.email());
        var hasUserWithThisEmail = userRepository.findByEmail(userDTO.email());
        if (hasUserWithThisEmail.isPresent()) {
            log.warn("Falha ao criar usuário: email já cadastrado {}", userDTO.email());
            throw new BadCredentialsException("Dados inválido");
        }
        var newUser = apiObjectMapper.dtoToEntity(userDTO, User.class);
        newUser.setPassword(bCryptPasswordEncoder.encode(userDTO.password()));
        var savedUser = userRepository.save(newUser);
        log.info("Usuário criado com sucesso: {}", savedUser.getEmail());
        return savedUser.getResponse(userResponseDTOApiObjectMapper);
    }

    @Transactional
    public void changePassword(JwtAuthenticationToken jwtAuthenticationToken, String newPassword) {
        var userId = getUserIdFromToken(jwtAuthenticationToken);
        log.info("Alterando senha para o usuário com ID: {}", userId);

        var currentUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado para alteração de senha, ID: {}", userId);
                    return new EntityNotFoundException("Usuário não encontrado");
                });

        currentUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(currentUser);
        log.info("Senha alterada com sucesso para o usuário ID: {}", userId);
    }

    @TransactionalReadOnly
    public UserResponseContextDTO getUserData(JwtAuthenticationToken jwtAuthenticationToken) {
        var userId = getUserIdFromToken(jwtAuthenticationToken);
        log.info("Buscando dados do usuário com ID: {}", userId);

        var userData = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado, ID: {}", userId);
                    return new EntityNotFoundException("Usuário não encontrado");
                });

        return userResponseContextDTOApiObjectMapper.entityToDto(userData, UserResponseContextDTO.class);
    }

    public boolean isPasswordCorrect(LoginDTO loginDTO, String password) {
        return passwordEncoder.matches(loginDTO.password(), password);
    }

    public String createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Date.from(Instant.now().plusMillis(refreshTokenDuration)));
        refreshTokenRepository.save(refreshToken);
        log.info("Refresh token criado para o usuário: {}", user.getEmail());
        return refreshToken.getToken();
    }

    public Long getUserIdFromToken(JwtAuthenticationToken jwtAuthenticationToken) {
        var userId = jwtAuthenticationToken.getTokenAttributes().get("sub").toString();
        return Long.parseLong(userId);
    }
}
