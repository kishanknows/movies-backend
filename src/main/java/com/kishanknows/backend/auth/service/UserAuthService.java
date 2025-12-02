package com.kishanknows.backend.auth.service;

import com.kishanknows.backend.auth.dto.UserLoginRequest;
import com.kishanknows.backend.auth.dto.UserRegistrationRequest;
import com.kishanknows.backend.auth.mapper.UserAuthMapper;
import com.kishanknows.backend.auth.model.RefreshToken;
import com.kishanknows.backend.auth.model.UserAuth;
import com.kishanknows.backend.core.security.JwtUtil;
import com.kishanknows.backend.core.security.PasswordHasher;
import com.kishanknows.backend.user.mapper.UserMapper;
import com.kishanknows.backend.user.model.User;
import com.kishanknows.backend.user.model.UserEntity;
import com.kishanknows.backend.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserAuthService {
    private final UserRepository repo;
    private final RefreshTokenService refreshTokenService;

    public UserAuthService(UserRepository repo, RefreshTokenService refreshTokenService){
        this.repo = repo;
        this.refreshTokenService = refreshTokenService;
    }

    public UserAuth registerUser(UserRegistrationRequest request){
        String username = request.username();
        String password = request.password();
        Optional<UserEntity> userEntity = repo.findByUsername(username);
        if(userEntity.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already taken");
        }
        else{
            PasswordHasher hasher = new PasswordHasher();
            String hashedPassword = hasher.hash(password);
            User user = UserAuthMapper.toUserDomain(request, hashedPassword);
            UserEntity entity = UserMapper.toEntity(user);
            repo.save(entity);
            String accessToken = JwtUtil.generateAccessToken(entity.getId(), entity.getName(), entity.getEmail());
            String refreshToken = refreshTokenService.createRefreshToken(entity.getId());
            return new UserAuth("user creation successful", true, accessToken, refreshToken);
        }
    }

    public UserAuth loginUser(UserLoginRequest request){
        String username = request.username();
        String password = request.password();
        Optional<UserEntity> entity = repo.findByUsername(username);
        if(entity.isPresent()){
            PasswordHasher hasher = new PasswordHasher();
            UserEntity userEntity = entity.get();
            String hashedPassword = userEntity.getPassword();
            Boolean isPasswordCorrect = hasher.matches(password, hashedPassword);
            if(isPasswordCorrect){
                String accessToken = JwtUtil.generateAccessToken(userEntity.getId(), userEntity.getName(), userEntity.getEmail());
                String refreshToken = refreshTokenService.createRefreshToken(userEntity.getId());
                return new UserAuth("user logged in successfully", true, accessToken, refreshToken);
            }
            else{
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "incorrect password");
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username not found");
        }
    }

    public UserEntity getUserById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

}
