package com.kishanknows.backend.auth.model;

public record UserAuth(
        String message,
        Boolean success,
        String accessToken,
        String refreshToken
) {
}
