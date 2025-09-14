package com.simplesdental.product.DTOs.responses;

public record LoginResponseDTO(
        String accessToken,
        Long expiresIn,
        String refreshToken
) {
}
