package com.kishanknows.backend.auth.controller;

import com.kishanknows.backend.auth.dto.RefreshTokenRequest;
import com.kishanknows.backend.auth.dto.UserAuthResponse;
import com.kishanknows.backend.auth.dto.UserLoginRequest;
import com.kishanknows.backend.auth.dto.UserRegistrationRequest;
import com.kishanknows.backend.auth.mapper.UserAuthMapper;
import com.kishanknows.backend.auth.model.RefreshToken;
import com.kishanknows.backend.auth.service.RefreshTokenService;
import com.kishanknows.backend.auth.service.UserAuthService;
import com.kishanknows.backend.core.security.JwtUtil;
import com.kishanknows.backend.user.model.UserEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserAuthController {
    private final UserAuthService userService;
    private final RefreshTokenService refreshTokenService;

    public UserAuthController(UserAuthService userService, RefreshTokenService refreshTokenService){
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    public UserAuthResponse registerUser(@RequestBody UserRegistrationRequest request){
        return UserAuthMapper
                .toResponse(userService.registerUser(request));
    }

    @PostMapping("/login")
    public UserAuthResponse loginUser(@RequestBody UserLoginRequest request){
        return UserAuthMapper
                .toResponse(userService.loginUser(request));
    }

    @PostMapping("/refresh")
    public UserAuthResponse refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshTokenString = refreshTokenRequest.refreshToken();
        RefreshToken token = refreshTokenService
                .findByToken(refreshTokenString)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        refreshTokenService.verifyExpiration(token);

        Long userId = token.getUserID();
        UserEntity user = userService.getUserById(userId);
        return new UserAuthResponse(
                "token refreshed",
                true,
                JwtUtil.generateAccessToken(user.getId(), user.getName(), user.getEmail()), refreshTokenString
        );
    }

}
