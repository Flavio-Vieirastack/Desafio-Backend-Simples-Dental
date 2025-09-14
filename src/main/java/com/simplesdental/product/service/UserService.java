package com.simplesdental.product.service;

import com.simplesdental.product.DTOs.requests.LoginDTO;
import com.simplesdental.product.DTOs.responses.LoginResponseDTO;
import com.simplesdental.product.model.RefreshToken;
import com.simplesdental.product.model.User;
import com.simplesdental.product.repository.RefreshTokenRepository;
import com.simplesdental.product.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
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
                JwtEncoderParameters.from(user.generateClaims(900L, user.getRole()))
        ).getTokenValue();
        return new LoginResponseDTO(
                token,
                900L,
                createRefreshToken(user)
        );
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
        refreshToken.setExpiryDate(Date.from(Instant.now().plusMillis(1500)));
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }
}
