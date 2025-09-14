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
    private final Long refreshTokenDuration = tokenDuration *3;

    @Transactional
    public LoginResponseDTO login(LoginDTO loginDTO) {
        var user = userRepository.findByEmail(loginDTO.email())
                .orElseThrow(
                        () -> new EntityNotFoundException("Usuário não encontrado")
                );
        if(!isPasswordCorrect(loginDTO, user.getPassword())) {
            throw new BadCredentialsException("Dados inválido");
        }
        var token = jwtEncoder.encode(
                JwtEncoderParameters.from(user.generateClaims(tokenDuration, user.getRole()))
        ).getTokenValue();
        return new LoginResponseDTO(
                token,
                tokenDuration,
                createRefreshToken(user)
        );
    }

    @TransactionalReadOnly
    public LoginResponseDTO refreshToken(String refreshToken) {
        var expiresIn = refreshTokenDuration;
        var storedToken = refreshTokenRepository.findByToken(refreshToken);
        if (storedToken.isEmpty() || storedToken.get().isExpired()) {
            throw new AccessDeniedException("Token inválido ou expirado");
        }
        User user = storedToken.get().getUser();
        var token = jwtEncoder.encode(
                JwtEncoderParameters.from(user.generateClaims(expiresIn, user.getRole()))
        ).getTokenValue();
        return new LoginResponseDTO(
                token,
                expiresIn,
                refreshToken
        );
    }

    @Transactional
    public UserResponseDTO createNewUser(UserDTO userDTO) {
        var hasUserWithThisEmail = userRepository.findByEmail(userDTO.email());
        if(hasUserWithThisEmail.isPresent()) {
            throw new BadCredentialsException("Dados inválido");
        }
        var newUser = apiObjectMapper.dtoToEntity(userDTO, User.class);
        newUser.setPassword(bCryptPasswordEncoder.encode(userDTO.password()));
        return userRepository.save(newUser).getResponse(userResponseDTOApiObjectMapper);
    }

    @Transactional
    public void changePassword(JwtAuthenticationToken jwtAuthenticationToken, String newPassword) {
        var currentUser = userRepository.findById(getUserIdFromToken(jwtAuthenticationToken))
                .orElseThrow(
                        () -> new EntityNotFoundException("Usuário não encontrado")
                );
        currentUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(currentUser);
    }

    @TransactionalReadOnly
    public UserResponseContextDTO getUserData(JwtAuthenticationToken jwtAuthenticationToken) {
        var userData = userRepository.findById(getUserIdFromToken(jwtAuthenticationToken))
                .orElseThrow(
                        () -> new EntityNotFoundException("Usuário não encontrado")
                );
        return userResponseContextDTOApiObjectMapper.entityToDto(userData, UserResponseContextDTO.class);
    }

    public boolean isPasswordCorrect(
            LoginDTO loginDTO,
            String password
    ) {
        return passwordEncoder.matches(
                loginDTO.password(),
                password
        );
    }

    public String createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Date.from(Instant.now().plusMillis(refreshTokenDuration)));
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    public Long getUserIdFromToken(JwtAuthenticationToken jwtAuthenticationToken) {
        var userId = jwtAuthenticationToken.getTokenAttributes().get("sub").toString();
        return Long.parseLong(userId);
    }
}
