package com.kishanknows.backend.auth.mapper;

import com.kishanknows.backend.auth.dto.UserAuthResponse;
import com.kishanknows.backend.auth.dto.UserRegistrationRequest;
import com.kishanknows.backend.auth.model.UserAuth;
import com.kishanknows.backend.user.model.User;

public class UserAuthMapper {
    public static User toUserDomain(UserRegistrationRequest request, String hashedPassword){
        return new User(
                null,
                request.name(),
                request.email(),
                request.username(),
                hashedPassword
        );
    }

    public static UserAuthResponse toResponse(UserAuth userAuth){
        return new UserAuthResponse(
                userAuth.message(),
                userAuth.success(),
                userAuth.accessToken(),
                userAuth.refreshToken()
        );
    }
}
