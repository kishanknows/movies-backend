package com.kishanknows.backend.auth.dto;

public record UserAuthResponse(
        String message,
        Boolean success,
        String accessToken,
        String refreshToken
) {
}
