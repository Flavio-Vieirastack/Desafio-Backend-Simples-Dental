package com.simplesdental.product.model;

import com.simplesdental.product.DTOs.responses.UserResponseDTO;
import com.simplesdental.product.Enums.Role;
import com.simplesdental.product.Interfaces.CustomResponse;
import com.simplesdental.product.utils.ApiObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import java.time.Instant;

@Entity
@Table(
        name = "tb_users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_email", columnNames = "email")
        },
        indexes = {
                @Index(name = "idx_user_email", columnList = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class User implements CustomResponse<UserResponseDTO> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    public JwtClaimsSet generateClaims(Long expiresIn, Role role) {
        var now = Instant.now();
        return JwtClaimsSet.builder()
                .issuer("Simples Dental")
                .subject(getId().toString())
                .claim("scope", role)
                .expiresAt(now.plusSeconds(expiresIn))
                .issuedAt(now)
                .build();
    }

    @Override
    public UserResponseDTO getResponse(ApiObjectMapper<UserResponseDTO> apiObjectMapper) {
        return apiObjectMapper.entityToDto(this, UserResponseDTO.class);
    }
}
